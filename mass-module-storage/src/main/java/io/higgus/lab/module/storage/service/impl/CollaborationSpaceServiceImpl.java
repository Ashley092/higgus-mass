package io.higgus.lab.module.storage.service.impl;

import io.higgus.lab.module.storage.dal.dataobject.CollaborationSpaceDO;
import io.higgus.lab.module.storage.dal.mysql.CollaborationSpaceMapper;
import io.higgus.lab.module.storage.vo.CollaborationSpaceCreateReqVO;
import io.higgus.lab.module.storage.vo.CollaborationSpaceRespVO;
import io.higgus.lab.module.storage.vo.CollaborationSpaceUpdateReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CollaborationSpaceServiceImpl {

    @Resource
    private CollaborationSpaceMapper collaborationSpaceMapper;

    public Long createSpace(CollaborationSpaceCreateReqVO createReqVO, String creator) {
        CollaborationSpaceDO space = CollaborationSpaceDO.builder()
                .name(createReqVO.getName())
                .description(createReqVO.getDescription())
                .status(0)
                .creator(creator)
                .updater(creator)
                .deleted(false)
                .build();
        collaborationSpaceMapper.insert(space);
        log.info("创建协作空间成功, id={}, name={}", space.getId(), space.getName());
        return space.getId();
    }

    public void updateSpace(CollaborationSpaceUpdateReqVO updateReqVO, String updater) {
        CollaborationSpaceDO space = CollaborationSpaceDO.builder()
                .id(updateReqVO.getId())
                .name(updateReqVO.getName())
                .description(updateReqVO.getDescription())
                .status(updateReqVO.getStatus())
                .updater(updater)
                .build();
        collaborationSpaceMapper.updateById(space);
        log.info("更新协作空间成功, id={}", updateReqVO.getId());
    }

    public void deleteSpace(Long id) {
        collaborationSpaceMapper.deleteById(id);
        log.info("删除协作空间成功, id={}", id);
    }

    public CollaborationSpaceRespVO getSpace(Long id) {
        CollaborationSpaceDO space = collaborationSpaceMapper.selectById(id);
        return convertToRespVO(space);
    }

    public List<CollaborationSpaceRespVO> getSpaceList() {
        LambdaQueryWrapper<CollaborationSpaceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationSpaceDO::getDeleted, false);
        List<CollaborationSpaceDO> list = collaborationSpaceMapper.selectList(wrapper);
        return list.stream().map(this::convertToRespVO).toList();
    }

    private CollaborationSpaceRespVO convertToRespVO(CollaborationSpaceDO space) {
        if (space == null) {
            return null;
        }
        return CollaborationSpaceRespVO.builder()
                .id(space.getId())
                .name(space.getName())
                .description(space.getDescription())
                .status(space.getStatus())
                .creator(space.getCreator())
                .createTime(space.getCreateTime() != null ? space.getCreateTime().toString() : null)
                .updater(space.getUpdater())
                .updateTime(space.getUpdateTime() != null ? space.getUpdateTime().toString() : null)
                .build();
    }
}
