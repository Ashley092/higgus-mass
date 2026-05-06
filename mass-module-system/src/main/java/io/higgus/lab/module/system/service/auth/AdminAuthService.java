package io.higgus.lab.module.system.service.auth;

import io.higgus.lab.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import io.higgus.lab.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import io.higgus.lab.module.system.dal.dataobject.auth.AdminUserDO;

public interface AdminAuthService {

    public AuthLoginRespVO login(AuthLoginReqVO reqVO) ;


    public AdminUserDO authenticate(String username, String password);


    public AuthLoginRespVO createTokenAfterLoginSuccess(Long userId, String username);
}
