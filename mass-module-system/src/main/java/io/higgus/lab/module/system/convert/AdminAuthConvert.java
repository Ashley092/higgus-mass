package io.higgus.lab.module.system.convert;


import io.higgus.lab.mass.framework.common.util.object.BeanUtils;
import io.higgus.lab.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import io.higgus.lab.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminAuthConvert {

    AdminAuthConvert INSTANCE = Mappers.getMapper(AdminAuthConvert.class);

    default AuthLoginRespVO convert(OAuth2AccessTokenDO oauth2AccessTokenDO) {
        AuthLoginRespVO authLoginRespVO = BeanUtils.toBean(oauth2AccessTokenDO, AuthLoginRespVO.class);
        return authLoginRespVO;
    }
}
