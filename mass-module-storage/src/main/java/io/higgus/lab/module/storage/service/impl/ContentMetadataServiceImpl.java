package io.higgus.lab.module.storage.service.impl;

import io.higgus.lab.module.storage.dal.dataobject.ContentMetadataDO;
import io.higgus.lab.module.storage.dal.mysql.ContentMetadataMapper;
import io.higgus.lab.module.storage.service.ContentMetadataService;
import io.higgus.lab.module.storage.vo.ContentMetadataCreateReqVO;
import io.higgus.lab.module.storage.vo.ContentMetadataRespVO;
import io.higgus.lab.module.storage.vo.ContentMetadataUpdateReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 内容元数据服务实现 - 仅负责元数据的 CRUD
 */
@Slf4j
@Service
public class ContentMetadataServiceImpl implements ContentMetadataService {

    @Resource
    private ContentMetadataMapper contentMetadataMapper;

    @Override
    public Long create(ContentMetadataCreateReqVO createReqVO, Long creator) {
        ContentMetadataDO content = ContentMetadataDO.builder()
                .itemId(createReqVO.getItemId())
                .title(createReqVO.getTitle())
                .contentType(createReqVO.getContentType())
                .storageKey(createReqVO.getStorageKey())
                .fileSize(createReqVO.getFileSize())
                .fileExtension(createReqVO.getFileExtension())
                .fileMd5(createReqVO.getFileMd5())
                .version(1)
                .creator(creator)
                .updater(creator)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .deleted(false)
                .build();
        contentMetadataMapper.insert(content);
        log.info("创建内容元数据, id={}, title={}", content.getId(), content.getTitle());
        return content.getId();
    }

    @Override
    public void update(ContentMetadataUpdateReqVO updateReqVO, Long updater) {
        ContentMetadataDO content = ContentMetadataDO.builder()
                .id(updateReqVO.getId())
                .itemId(updateReqVO.getItemId())
                .title(updateReqVO.getTitle())
                .contentType(updateReqVO.getContentType())
                .storageKey(updateReqVO.getStorageKey())
                .fileSize(updateReqVO.getFileSize())
                .fileExtension(updateReqVO.getFileExtension())
                .fileMd5(updateReqVO.getFileMd5())
                .updater(updater)
                .updateTime(LocalDateTime.now())
                .build();
        contentMetadataMapper.updateById(content);
        log.info("更新内容元数据, id={}", updateReqVO.getId());
    }

    @Override
    public void delete(Long id) {
        contentMetadataMapper.deleteById(id);
        log.info("删除内容元数据, id={}", id);
    }

    @Override
    public ContentMetadataRespVO get(Long id) {
        ContentMetadataDO content = contentMetadataMapper.selectById(id);
        return convertToRespVO(content);
    }

    @Override
    public ContentMetadataDO getDO(Long id) {
        return contentMetadataMapper.selectById(id);
    }

    @Override
    public List<ContentMetadataRespVO> getListByItemId(Long itemId) {
        LambdaQueryWrapper<ContentMetadataDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentMetadataDO::getItemId, itemId)
               .eq(ContentMetadataDO::getDeleted, false);
        return contentMetadataMapper.selectList(wrapper)
                .stream()
                .map(this::convertToRespVO)
                .toList();
    }

    @Override
    public List<ContentMetadataRespVO> getListByItemIdAndType(Long itemId, Integer contentType) {
        LambdaQueryWrapper<ContentMetadataDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentMetadataDO::getItemId, itemId)
               .eq(ContentMetadataDO::getContentType, contentType)
               .eq(ContentMetadataDO::getDeleted, false);
        return contentMetadataMapper.selectList(wrapper)
                .stream()
                .map(this::convertToRespVO)
                .toList();
    }

    @Override
    public ContentMetadataDO findByMd5(String fileMd5) {
        LambdaQueryWrapper<ContentMetadataDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentMetadataDO::getFileMd5, fileMd5)
               .eq(ContentMetadataDO::getDeleted, false);
        return contentMetadataMapper.selectOne(wrapper);
    }

    @Override
    public List<ContentMetadataRespVO> getList() {
        LambdaQueryWrapper<ContentMetadataDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContentMetadataDO::getDeleted, false);
        return contentMetadataMapper.selectList(wrapper)
                .stream()
                .map(this::convertToRespVO)
                .toList();
    }

    private ContentMetadataRespVO convertToRespVO(ContentMetadataDO content) {
        if (content == null) {
            return null;
        }
        return ContentMetadataRespVO.builder()
                .id(content.getId())
                .itemId(content.getItemId())
                .title(content.getTitle())
                .contentType(content.getContentType())
                .storageKey(content.getStorageKey())
                .fileSize(content.getFileSize())
                .fileExtension(content.getFileExtension())
                .fileMd5(content.getFileMd5())
                .version(content.getVersion())
                .creator(content.getCreator())
                .updater(content.getUpdater())
                .createTime(content.getCreateTime() != null ? content.getCreateTime().toString() : null)
                .updateTime(content.getUpdateTime() != null ? content.getUpdateTime().toString() : null)
                .build();
    }
}
