package io.higgus.lab.module.system.service.oauth2;

import cn.hutool.core.util.IdUtil;
import io.higgus.lab.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import io.higgus.lab.module.system.dal.mysql.oauth2.OAuth2AccessTokenMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService {


    @Resource
    private OAuth2AccessTokenMapper oauth2AccessTokenMapper;

    @Override
    public OAuth2AccessTokenDO createAccessToken(Long userId) {

        return createOAuth2AccessToken(userId);
    }


    private OAuth2AccessTokenDO createOAuth2AccessToken(Long userId) {
        OAuth2AccessTokenDO accessTokenDO = new OAuth2AccessTokenDO();
        accessTokenDO.setAccessToken(generateAccessToken());
        accessTokenDO.setUserId(userId);
        accessTokenDO.setUserType(1);
        accessTokenDO.setExpiresTime(LocalDateTime.now().plusSeconds(6000));
        oauth2AccessTokenMapper.insert(accessTokenDO);
        // 记录到 Redis 中
        return accessTokenDO;
    }

    private static String generateAccessToken() {
        return IdUtil.fastSimpleUUID();
    }

}
