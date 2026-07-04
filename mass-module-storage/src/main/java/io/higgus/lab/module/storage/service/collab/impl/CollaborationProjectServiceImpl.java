package io.higgus.lab.module.storage.service.collab.impl;

import io.higgus.lab.module.storage.controller.vo.CollaborationItemCreateReqVO;
import io.higgus.lab.module.storage.controller.vo.CollaborationItemRespVO;
import io.higgus.lab.module.storage.controller.vo.CollaborationItemUpdateReqVO;
import io.higgus.lab.module.storage.dal.dataobject.CollaborationProjectDO;
import io.higgus.lab.module.storage.dal.mysql.CollaborationProjectMapper;
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
    private CollaborationProjectMapper collaborationProjectMapper;

    public Long createItem(CollaborationItemCreateReqVO createReqVO, String creator) {
        CollaborationProjectDO item = CollaborationProjectDO.builder()
                .spaceId(createReqVO.getSpaceId())
                .name(createReqVO.getName())
                .type(createReqVO.getType())
                .creator(creator)
                .updater(creator)
                .deleted(false)
                .build();
        collaborationProjectMapper.insert(item);
        log.info("创建项目/渠道成功, id={}, name={}, type={}", item.getId(), item.getName(), item.getType());
        return item.getId();
    }

    public void updateItem(CollaborationItemUpdateReqVO updateReqVO, String updater) {
        CollaborationProjectDO item = CollaborationProjectDO.builder()
                .id(updateReqVO.getId())
                .spaceId(updateReqVO.getSpaceId())
                .name(updateReqVO.getName())
                .type(updateReqVO.getType())
                .updater(updater)
                .build();
        collaborationProjectMapper.updateById(item);
        log.info("更新项目/渠道成功, id={}", updateReqVO.getId());
    }

    public void deleteItem(Long id) {
        collaborationProjectMapper.deleteById(id);
        log.info("删除项目/渠道成功, id={}", id);
    }

    public CollaborationItemRespVO getItem(Long id) {
        CollaborationProjectDO item = collaborationProjectMapper.selectById(id);
        return convertToRespVO(item);
    }

    public List<CollaborationItemRespVO> getItemListBySpaceId(Long spaceId) {
        LambdaQueryWrapper<CollaborationProjectDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationProjectDO::getSpaceId, spaceId)
               .eq(CollaborationProjectDO::getDeleted, false);
        List<CollaborationProjectDO> list = collaborationProjectMapper.selectList(wrapper);
        return list.stream().map(this::convertToRespVO).toList();
    }

    public List<CollaborationItemRespVO> getItemList() {
        LambdaQueryWrapper<CollaborationProjectDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationProjectDO::getDeleted, false);
        List<CollaborationProjectDO> list = collaborationProjectMapper.selectList(wrapper);
        return list.stream().map(this::convertToRespVO).toList();
    }

    private CollaborationItemRespVO convertToRespVO(CollaborationProjectDO item) {
        if (item == null) {
            return null;
        }
        return CollaborationItemRespVO.builder()
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
