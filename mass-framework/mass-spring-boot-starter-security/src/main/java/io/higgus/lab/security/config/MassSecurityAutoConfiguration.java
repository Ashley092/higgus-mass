package io.higgus.lab.security.config;


import io.higgus.lab.mass.framework.common.biz.system.permission.PermissionCommonApi;
import io.higgus.lab.security.core.service.SecurityFrameworkService;
import io.higgus.lab.security.core.service.SecurityFrameworkServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureOrder(-1)
@EnableConfigurationProperties(value = { SecurityProperties.class})
public class MassSecurityAutoConfiguration {

//
//    @Bean("ss") // 使用 Spring Security 的缩写，方便使用
//    public SecurityFrameworkService securityFrameworkService(PermissionCommonApi permissionApi) {
//        return new SecurityFrameworkServiceImpl(permissionApi);
//    }
}
