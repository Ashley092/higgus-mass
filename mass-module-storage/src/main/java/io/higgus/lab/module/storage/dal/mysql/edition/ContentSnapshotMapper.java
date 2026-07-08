package io.higgus.lab.module.storage.dal.mysql.edition;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.storage.dal.dataobject.edition.ContentSnapshotDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentSnapshotMapper extends BaseMapperX<ContentSnapshotDO> {

    /**
     * 获取当前快照版本号
     */
    default Integer getCurrentSnapshotVersion(Long contentId) {
        return null;
    }

    /**
     * 设置某个版本为当前版本，同时取消其他版本的当前标记
     */
    default void setAsCurrent(Long contentId, Integer snapshotVersion) {
    }
}
