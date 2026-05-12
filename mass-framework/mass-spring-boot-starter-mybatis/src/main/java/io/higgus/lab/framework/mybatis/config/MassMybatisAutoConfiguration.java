package io.higgus.lab.framework.mybatis.config;


import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.higgus.lab.framework.mybatis.core.handler.DefaultDBFieldHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
public class MassMybatisAutoConfiguration {


    @Bean
    public MetaObjectHandler defaultMetaObjectHandler() {
        return new DefaultDBFieldHandler(); // 自动填充参数类
    }

}
