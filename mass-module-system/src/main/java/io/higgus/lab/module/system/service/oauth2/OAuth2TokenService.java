package io.higgus.lab.module.system.service.oauth2;

import io.higgus.lab.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;

import java.util.List;

public interface OAuth2TokenService {

    OAuth2AccessTokenDO createAccessToken(Long userId);

}