package io.higgus.lab.module.storage.dal.mysql;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.storage.dal.dataobject.edition.EditionLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 协作编辑操作日志 Mapper
 */
@Mapper
public interface EditionLogMapper extends BaseMapperX<EditionLogDO> {

    default List<EditionLogDO> selectByContentId(Long contentId) {
        return selectList(new LambdaQueryWrapper<EditionLogDO>()
                .eq(EditionLogDO::getContentId, contentId)
                .orderByDesc(EditionLogDO::getCreateTime)
        );
    }

    default List<EditionLogDO> selectByTimeRange(Long contentId,
                                                 LocalDateTime startTime,
                                                 LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapper<EditionLogDO>()
                .eq(EditionLogDO::getContentId, contentId)
                .gt(EditionLogDO::getCreateTime, startTime)
                .lt(EditionLogDO::getCreateTime, endTime)
        );
    }

    default List<EditionLogDO> selectBySnapshotVersion(Long contentId, Integer snapshotVersion) {
        return selectList(new LambdaQueryWrapper<EditionLogDO>()
                .eq(EditionLogDO::getContentId, contentId)
                .eq(EditionLogDO::getContentSnapshotVersion, snapshotVersion)
                .orderByDesc(EditionLogDO::getCreateTime)
        );
    }



}
