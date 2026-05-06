package io.higgus.lab.module.system.controller.admin.auth;

import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import io.higgus.lab.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import io.higgus.lab.module.system.service.auth.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 认证")
@RestController
@RequestMapping("/system/auth")
@Slf4j
public class AuthController {

    @Resource
    private AdminAuthService authService;

    @Operation(summary = "使用账号密码登录")
    @PostMapping("/login")
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return success(authService.login(reqVO));
    }



}
