package io.higgus.lab.module.system.dal.mysql.oauth2;


import io.higgus.lab.framework.mybatis.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2AccessTokenMapper extends BaseMapperX<OAuth2AccessTokenDO> {

}
