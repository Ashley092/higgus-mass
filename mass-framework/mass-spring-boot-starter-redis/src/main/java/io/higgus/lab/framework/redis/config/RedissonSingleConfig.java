package io.higgus.lab.framework.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = RedissonAutoConfigurationV2.class) // 目的：使用自己定义的 RedisTemplate Bean
@EnableConfigurationProperties(MassRedisSingleProperties.class)
@ConditionalOnProperty(prefix = "mass.redis.single", name = "enabled", havingValue = "true")
public class RedissonSingleConfig {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(MassRedisSingleProperties properties) {
        Config config = new Config();

        // 显式声明类型，不使用 var
        config.useSingleServer()
                .setAddress(properties.getAddress())
                .setDatabase(properties.getDatabase())
                .setConnectTimeout(properties.getConnectTimeout())
                .setTimeout(properties.getTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval());

        return Redisson.create(config);
    }
}
