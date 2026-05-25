package io.higgus.lab.module.storage.dal.mysql;

import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.storage.dal.dataobject.ContentMetadataDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentMetadataMapper extends BaseMapperX<ContentMetadataDO> {
}
