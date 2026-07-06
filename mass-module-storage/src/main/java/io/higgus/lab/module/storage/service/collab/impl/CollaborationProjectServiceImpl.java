package io.higgus.lab.module.storage.service.collab.impl;

import io.higgus.lab.module.storage.controller.collab.vo.collab.project.CollaborationProjectCreateReqVO;
import io.higgus.lab.module.storage.controller.collab.vo.collab.project.CollaborationProjectRespVO;
import io.higgus.lab.module.storage.controller.collab.vo.collab.project.CollaborationProjectUpdateReqVO;
import io.higgus.lab.module.storage.dal.dataobject.collab.CollaborationProjectDO;
import io.higgus.lab.module.storage.dal.mysql.collab.CollaborationProjectMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.module.storage.service.collab.CollaborationProjectService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CollaborationProjectServiceImpl implements CollaborationProjectService {

    @Resource
    private CollaborationProjectMapperX collaborationProjectMapperX;

    public Long createItem(CollaborationProjectCreateReqVO createReqVO, String creator) {
        CollaborationProjectDO item = CollaborationProjectDO.builder()
                .spaceId(createReqVO.getSpaceId())
                .name(createReqVO.getName())
                .type(createReqVO.getType())
                .creator(creator)
                .updater(creator)
                .deleted(false)
                .build();
        collaborationProjectMapperX.insert(item);
        log.info("创建项目/渠道成功, id={}, name={}, type={}", item.getId(), item.getName(), item.getType());
        return item.getId();
    }

    public void updateItem(CollaborationProjectUpdateReqVO updateReqVO, String updater) {
        CollaborationProjectDO item = CollaborationProjectDO.builder()
                .id(updateReqVO.getId())
                .spaceId(updateReqVO.getSpaceId())
                .name(updateReqVO.getName())
                .type(updateReqVO.getType())
                .updater(updater)
                .build();
        collaborationProjectMapperX.updateById(item);
        log.info("更新项目/渠道成功, id={}", updateReqVO.getId());
    }

    public void deleteItem(Long id) {
        collaborationProjectMapperX.deleteById(id);
        log.info("删除项目/渠道成功, id={}", id);
    }

    public CollaborationProjectRespVO getItem(Long id) {
        CollaborationProjectDO item = collaborationProjectMapperX.selectById(id);
        return convertToRespVO(item);
    }

    public List<CollaborationProjectRespVO> getItemListBySpaceId(Long spaceId) {
        LambdaQueryWrapper<CollaborationProjectDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationProjectDO::getSpaceId, spaceId)
               .eq(CollaborationProjectDO::getDeleted, false);
        List<CollaborationProjectDO> list = collaborationProjectMapperX.selectList(wrapper);
        return list.stream().map(this::convertToRespVO).toList();
    }

    public List<CollaborationProjectRespVO> getItemList() {
        LambdaQueryWrapper<CollaborationProjectDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationProjectDO::getDeleted, false);
        List<CollaborationProjectDO> list = collaborationProjectMapperX.selectList(wrapper);
        return list.stream().map(this::convertToRespVO).toList();
    }

    private CollaborationProjectRespVO convertToRespVO(CollaborationProjectDO item) {
        if (item == null) {
            return null;
        }
        return CollaborationProjectRespVO.builder()
                .id(item.getId())
                .spaceId(item.getSpaceId())
                .name(item.getName())
                .type(item.getType())
                .creator(item.getCreator())
                .createTime(item.getCreateTime() != null ? item.getCreateTime().toString() : null)
                .updater(item.getUpdater())
                .updateTime(item.getUpdateTime() != null ? item.getUpdateTime().toString() : null)
                .build();
    }
}
