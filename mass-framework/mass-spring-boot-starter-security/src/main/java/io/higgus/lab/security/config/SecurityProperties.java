package io.higgus.lab.security.config;


import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "mass.security")
@Validated
@Data
public class SecurityProperties {

    @NotEmpty(message = "Token Header 不能为空")
    private String tokenHeader = "Authorization";

    @NotEmpty(message = "Token Parameter 不能为空")
    private String tokenParameter = "token";

}
