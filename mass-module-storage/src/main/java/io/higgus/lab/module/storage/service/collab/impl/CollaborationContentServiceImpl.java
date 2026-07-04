package io.higgus.lab.module.storage.service.collab.impl;

import io.higgus.lab.module.storage.dal.dataobject.CollaborationContentDO;
import io.higgus.lab.module.storage.dal.mysql.ContentMetadataMapper;
import io.higgus.lab.module.storage.service.collab.CollaborationContentService;
import io.higgus.lab.module.storage.controller.vo.ContentMetadataCreateReqVO;
import io.higgus.lab.module.storage.controller.vo.ContentMetadataRespVO;
import io.higgus.lab.module.storage.controller.vo.ContentMetadataUpdateReqVO;
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
public class CollaborationContentServiceImpl implements CollaborationContentService {

    @Resource
    private ContentMetadataMapper contentMetadataMapper;

    @Override
    public Long create(ContentMetadataCreateReqVO createReqVO, Long creator) {
        CollaborationContentDO content = CollaborationContentDO.builder()
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
        CollaborationContentDO content = CollaborationContentDO.builder()
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
        CollaborationContentDO content = contentMetadataMapper.selectById(id);
        return convertToRespVO(content);
    }

    @Override
    public CollaborationContentDO getDO(Long id) {
        return contentMetadataMapper.selectById(id);
    }

    @Override
    public List<ContentMetadataRespVO> getListByItemId(Long itemId) {
        LambdaQueryWrapper<CollaborationContentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationContentDO::getItemId, itemId)
               .eq(CollaborationContentDO::getDeleted, false);
        return contentMetadataMapper.selectList(wrapper)
                .stream()
                .map(this::convertToRespVO)
                .toList();
    }

    @Override
    public List<ContentMetadataRespVO> getListByItemIdAndType(Long itemId, Integer contentType) {
        LambdaQueryWrapper<CollaborationContentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationContentDO::getItemId, itemId)
               .eq(CollaborationContentDO::getContentType, contentType)
               .eq(CollaborationContentDO::getDeleted, false);
        return contentMetadataMapper.selectList(wrapper)
                .stream()
                .map(this::convertToRespVO)
                .toList();
    }

    @Override
    public CollaborationContentDO findByMd5(String fileMd5) {
        LambdaQueryWrapper<CollaborationContentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationContentDO::getFileMd5, fileMd5)
               .eq(CollaborationContentDO::getDeleted, false);
        return contentMetadataMapper.selectOne(wrapper);
    }

    @Override
    public List<ContentMetadataRespVO> getList() {
        LambdaQueryWrapper<CollaborationContentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationContentDO::getDeleted, false);
        return contentMetadataMapper.selectList(wrapper)
                .stream()
                .map(this::convertToRespVO)
                .toList();
    }

    private ContentMetadataRespVO convertToRespVO(CollaborationContentDO content) {
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
