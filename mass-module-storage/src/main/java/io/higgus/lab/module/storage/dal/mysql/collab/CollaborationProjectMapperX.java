package io.higgus.lab.module.storage.dal.mysql.collab;

import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.storage.dal.dataobject.collab.CollaborationProjectDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollaborationProjectMapperX extends BaseMapperX<CollaborationProjectDO> {
}
