package io.higgus.lab.module.system.controller.admin.auth.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应信息")
public class AuthLoginRespVO {

    @Schema(description = "用户编号", example = "1")
    private Long userId;

    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "令牌过期时间", example = "2026-05-07 17:32:49")
    private LocalDateTime expiresTime;

    @Schema(description = "用户类型", example = "1")
    private Integer userType;

}