package io.higgus.lab.module.storage.service.collab.impl;

import cn.hutool.core.util.IdUtil;
import io.higgus.lab.module.storage.controller.vo.ContentMetadataCreateReqVO;
import io.higgus.lab.module.storage.dal.dataobject.CollaborationContentDO;
import io.higgus.lab.module.storage.service.collab.CollaborationContentFacadeService;
import io.higgus.lab.module.storage.service.ContentMetadataService;
import io.higgus.lab.module.storage.service.FileStorageService;
import io.higgus.lab.module.storage.controller.vo.ContentUploadReqVO;
import io.higgus.lab.module.storage.controller.vo.UploadResultVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * 内容门面服务实现 - 编排元数据和文件存储
 */
@Slf4j
@Service
public class CollaborationContentFacadeServiceImpl implements CollaborationContentFacadeService {

    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private ContentMetadataService contentMetadataService;

    @Override
    public UploadResultVO uploadFile(MultipartFile file, Long creator) throws IOException {
        return uploadFile(file, ContentUploadReqVO.builder().build(), creator);
    }

    @Override
    public UploadResultVO uploadFile(MultipartFile file, ContentUploadReqVO reqVO, Long creator) throws IOException {
        // 1. 计算 MD5
        String md5 = fileStorageService.calculateMd5(file);
        log.info("计算文件MD5完成, md5={}", md5);

        // 2. 先查一次（快速返回，避免不必要的锁竞争）
        CollaborationContentDO existing = contentMetadataService.findByMd5(md5);
        if (existing != null) {
            log.info("秒传成功，直接返回已有记录");
            return UploadResultVO.skip(String.valueOf(existing.getId()), existing.getStorageKey(), md5);
        }

        // 3. 使用 synchronized 关键字（JVM 级别锁，最简单）
        // .intern() 找到相同字符串的对象
        synchronized (md5.intern()) {
            // 4. 双重检查（锁内再查一次，防止等待锁的线程重复上传）
            CollaborationContentDO existingInLock = contentMetadataService.findByMd5(md5);
            if (existingInLock != null) {
                log.info("双重检查命中秒传");
                return UploadResultVO.skip(String.valueOf(existingInLock.getId()), existingInLock.getStorageKey(), md5);
            }

            // 5. 上传到 MinIO（现在只有获得锁的线程会执行）
            String uuid = IdUtil.simpleUUID();
            String storageKey = fileStorageService.upload(file, uuid);
            log.info("文件上传到MinIO完成, storageKey={}", storageKey);

            // 6. 创建元数据
            ContentMetadataCreateReqVO createReqVO = ContentMetadataCreateReqVO.builder()
                    .itemId(reqVO.getItemId())
                    .title(reqVO.getTitle() != null ? reqVO.getTitle() : file.getOriginalFilename())
                    .contentType(reqVO.getContentType())
                    .storageKey(storageKey)
                    .fileSize(file.getSize())
                    .fileExtension(getFileExtension(file.getOriginalFilename()))
                    .fileMd5(md5)
                    .build();

            Long contentId = contentMetadataService.create(createReqVO, creator);
            log.info("内容元数据创建完成, id={}", contentId);

            return UploadResultVO.newUpload(String.valueOf(contentId), storageKey, md5);
        }
    }

    @Override
    public void deleteContent(Long contentId) {
        // 1. 获取元数据
        CollaborationContentDO content = contentMetadataService.getDO(contentId);
        if (content == null) {
            log.warn("内容不存在, id={}", contentId);
            return;
        }

        // 2. 删除 MinIO 文件
        if (content.getStorageKey() != null) {
            fileStorageService.delete(content.getStorageKey());
        }

        // 3. 删除元数据
        contentMetadataService.delete(contentId);
        log.info("删除内容完成, id={}", contentId);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            return filename.substring(lastDot + 1).toLowerCase();
        }
        return null;
    }
}
