package io.higgus.lab.framework.security.security.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

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
