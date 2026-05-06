package io.higgus.lab.module.system.enums;

import io.higgus.lab.mass.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {


    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_002_000_000, "登录失败，账号密码不正确");

}
