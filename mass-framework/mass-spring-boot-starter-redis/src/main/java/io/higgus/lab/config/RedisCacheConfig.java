package io.higgus.lab.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@AutoConfiguration
// 1. 确保在官方 Redis 自动配置之前执行，用以覆盖默认的 RedisTemplate
@AutoConfigureBefore(org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
// 2. 核心修改：强制让当前类在你的 RedissonSingleConfig 之后才进行解析
@AutoConfigureAfter(RedissonSingleConfig.class)
@EnableCaching
@ConditionalOnBean(RedissonClient.class) // 现在可以安全地等待 RedissonClient 诞生了
@ConditionalOnProperty(prefix = "mass.redis.single", name = "enabled", havingValue = "true")
public class RedisCacheConfig {

    /**
     * RedisTemplate 配置
     * 用于直接操作 Redis（GET/SET/Hash等）
     *
     * 序列化策略：
     * - Key: String 序列化（便于阅读）
     * - Value: JSON 序列化（支持对象存储）
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 创建 JSON 序列化器
        Jackson2JsonRedisSerializer<Object> jsonSerializer = createJsonSerializer();

        // Key 序列化：String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value 序列化：JSON
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

//    private Jackson2JsonRedisSerializer<Object> createJsonSerializer() {
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        mapper.activateDefaultTyping(
//            LaissezFaireSubTypeValidator.instance,
//            ObjectMapper.DefaultTyping.NON_FINAL,
//            JsonTypeInfo.As.PROPERTY
//        );
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        serializer.setObjectMapper(mapper);
//        return serializer;
//    }
    private Jackson2JsonRedisSerializer<Object> createJsonSerializer() {
        // 1. 先创建并配置好 ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 2. 使用新的构造器，直接把配置好的 mapper 塞进去
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);

        return serializer;
    }

    /**
     * CacheManager 配置
     * 用于 @Cacheable 等注解
     */
    @Bean
    @Primary
    public CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient);
    }
}