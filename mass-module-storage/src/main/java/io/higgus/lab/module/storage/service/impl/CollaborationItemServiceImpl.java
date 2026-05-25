package io.higgus.lab.module.storage.service.impl;

import io.higgus.lab.module.storage.dal.dataobject.CollaborationItemDO;
import io.higgus.lab.module.storage.dal.mysql.CollaborationItemMapper;
import io.higgus.lab.module.storage.vo.CollaborationItemCreateReqVO;
import io.higgus.lab.module.storage.vo.CollaborationItemRespVO;
import io.higgus.lab.module.storage.vo.CollaborationItemUpdateReqVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CollaborationItemServiceImpl {

    @Resource
    private CollaborationItemMapper collaborationItemMapper;

    public Long createItem(CollaborationItemCreateReqVO createReqVO, String creator) {
        CollaborationItemDO item = CollaborationItemDO.builder()
                .spaceId(createReqVO.getSpaceId())
                .name(createReqVO.getName())
                .type(createReqVO.getType())
                .creator(creator)
                .updater(creator)
                .deleted(false)
                .build();
        collaborationItemMapper.insert(item);
        log.info("创建项目/渠道成功, id={}, name={}, type={}", item.getId(), item.getName(), item.getType());
        return item.getId();
    }

    public void updateItem(CollaborationItemUpdateReqVO updateReqVO, String updater) {
        CollaborationItemDO item = CollaborationItemDO.builder()
                .id(updateReqVO.getId())
                .spaceId(updateReqVO.getSpaceId())
                .name(updateReqVO.getName())
                .type(updateReqVO.getType())
                .updater(updater)
                .build();
        collaborationItemMapper.updateById(item);
        log.info("更新项目/渠道成功, id={}", updateReqVO.getId());
    }

    public void deleteItem(Long id) {
        collaborationItemMapper.deleteById(id);
        log.info("删除项目/渠道成功, id={}", id);
    }

    public CollaborationItemRespVO getItem(Long id) {
        CollaborationItemDO item = collaborationItemMapper.selectById(id);
        return convertToRespVO(item);
    }

    public List<CollaborationItemRespVO> getItemListBySpaceId(Long spaceId) {
        LambdaQueryWrapper<CollaborationItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationItemDO::getSpaceId, spaceId)
               .eq(CollaborationItemDO::getDeleted, false);
        List<CollaborationItemDO> list = collaborationItemMapper.selectList(wrapper);
        return list.stream().map(this::convertToRespVO).toList();
    }

    public List<CollaborationItemRespVO> getItemList() {
        LambdaQueryWrapper<CollaborationItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollaborationItemDO::getDeleted, false);
        List<CollaborationItemDO> list = collaborationItemMapper.selectList(wrapper);
        return list.stream().map(this::convertToRespVO).toList();
    }

    private CollaborationItemRespVO convertToRespVO(CollaborationItemDO item) {
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
