package io.higgus.lab.module.storage.service.edition.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.module.storage.dal.dataobject.collab.CollaborationContentDO;
import io.higgus.lab.module.storage.dal.dataobject.edition.ContentSnapshotDO;
import io.higgus.lab.module.storage.dal.mysql.collab.ContentMetadataMapper;
import io.higgus.lab.module.storage.dal.mysql.edition.ContentSnapshotMapper;
import io.higgus.lab.module.storage.service.collab.FileStorageService;
import io.higgus.lab.module.storage.service.edition.ContentSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

@Service
public class ContentSnapshotServiceImpl implements ContentSnapshotService {

    private static final String TEMP_PREFIX = "temp_";

    @Autowired
    private ContentMetadataMapper contentMetadataMapper;
    @Autowired
    private ContentSnapshotMapper snapshotMapper;
    @Autowired
    private FileStorageService fileService;

    @Override
    @Transactional
    public ContentSnapshotDO createSnapshotTemp(Long contentId, Long creator) {
        // 1. 查询当前快照版本号
        CollaborationContentDO meta = contentMetadataMapper.selectById(contentId);
        if (meta == null) {
            throw new RuntimeException("内容不存在: " + contentId);
        }
        Integer currentVersion = meta.getCurrentSnapshotVersion();
        int newVersion = (currentVersion == null ? 0 : currentVersion) + 1;

        // 2. 生成临时 storageKey
        String tempStorageKey = TEMP_PREFIX + IdUtil.simpleUUID();

        // 3. 创建快照记录
        ContentSnapshotDO snapshot = ContentSnapshotDO.builder()
                .contentId(contentId)
                .snapshotVersion(newVersion)
                .storageKey(tempStorageKey)
                .isCurrent(0)
                .creator(creator)
                .createTime(LocalDateTime.now())
                .deleted(false)
                .build();
        snapshotMapper.insert(snapshot);

        // 4. 更新 content_metadata 的当前版本号
        CollaborationContentDO update = new CollaborationContentDO();
        update.setId(contentId);
        update.setCurrentSnapshotVersion(newVersion);
        // 注意：currentStorageKey 等 finalizeSnapshot 时再更新
        contentMetadataMapper.updateById(update);

        return snapshot;
    }

    @Override
    @Transactional
    public void finalizeSnapshot(Long snapshotId, byte[] fileBytes) {
        // 1. 查询快照
        ContentSnapshotDO snapshot = snapshotMapper.selectById(snapshotId);
        if (snapshot == null) {
            throw new RuntimeException("快照不存在: " + snapshotId);
        }

        // 2. 计算 MD5
        String md5 = calculateMd5(fileBytes);
        long fileSize = fileBytes.length;

        // 3. 生成正式 storageKey（去掉 temp_ 前缀）
        String formalStorageKey = snapshot.getStorageKey().replaceFirst(TEMP_PREFIX, "");

        // 4. 上传到 MinIO
        fileService.upload(fileBytes,formalStorageKey);

        // 5. 更新快照记录
        ContentSnapshotDO update = ContentSnapshotDO.builder()
                .id(snapshotId)
                .storageKey(formalStorageKey)
                .fileMd5(md5)
                .fileSize(fileSize)
                .isCurrent(1)
                .build();
        snapshotMapper.updateById(update);

        // 6. 更新 content_metadata 的 currentStorageKey
        CollaborationContentDO metaUpdate = new CollaborationContentDO();
        metaUpdate.setId(snapshot.getContentId());
        metaUpdate.setCurrentStorageKey(formalStorageKey);
        contentMetadataMapper.updateById(metaUpdate);
    }

    @Override
    public ContentSnapshotDO getCurrentSnapshot(Long contentId) {
        return snapshotMapper.selectOne(new LambdaQueryWrapper<ContentSnapshotDO>()
                .eq(ContentSnapshotDO::getContentId, contentId)
                .eq(ContentSnapshotDO::getIsCurrent, 1)
                .eq(ContentSnapshotDO::getDeleted, false));
    }

    private String calculateMd5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 计算失败", e);
        }
    }
}
