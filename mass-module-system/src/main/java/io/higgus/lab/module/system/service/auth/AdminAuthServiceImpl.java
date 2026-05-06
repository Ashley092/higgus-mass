package io.higgus.lab.module.system.service.auth;


import io.higgus.lab.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import io.higgus.lab.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import io.higgus.lab.module.system.convert.AdminAuthConvert;
import io.higgus.lab.module.system.dal.dataobject.auth.AdminUserDO;
import io.higgus.lab.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import io.higgus.lab.module.system.service.oauth2.OAuth2TokenService;
import io.higgus.lab.module.system.service.user.AdminUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {


    @Resource
    private AdminUserService userService;

    @Resource
    OAuth2TokenService oauth2TokenService;

    @Override
    public AuthLoginRespVO login(AuthLoginReqVO reqVO) {

        AdminUserDO user = authenticate(reqVO.getUsername(), reqVO.getPassword());

        return createTokenAfterLoginSuccess(user.getId(), reqVO.getUsername());
    }

    @Override
    public AdminUserDO authenticate(String username, String password) {

        AdminUserDO user = userService.getUserByUsername(username);

        if (user == null) {
            throw new RuntimeException("用户不存在！");
        }

        return user;
    }

    @Override
    public AuthLoginRespVO createTokenAfterLoginSuccess(Long userId, String username) {

        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId);

        return AdminAuthConvert.INSTANCE.convert(accessTokenDO);
    }


}
