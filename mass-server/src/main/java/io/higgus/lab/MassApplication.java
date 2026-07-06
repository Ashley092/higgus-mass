package io.higgus.lab;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.util.Set;


@SpringBootApplication
public class MassApplication {
    public static final String BRAND_BLUE = "\u001B[38;2;29;80;163m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MassApplication.class, args);
        System.out.print(
                BRAND_BLUE +
                        "     (♥◠‿◠)ﾉﾞ        Mass 启动成功！        ლ(´ڡ`ლ)ﾞ\n" +
                        " ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▄\n" +
                        "░░░░░░░░▄▐░▄▄█████▄▄\n" +
                        "░░░░░░▄█████████████▄▀▄▄░▄▄▄\n" +
                        "░░░░░█████████████████▄██████\n" +
                        "░░░░████▐█████▌████████▌█████▌\n" +
                        "░░░████▌█████▌█░████████▐▀██▀\n" +
                        "░▄█████░█████▌░█░▀██████▌█▄▄▀▄\n" +
                        "░▌███▌█░▐███▌▌░░▄▄░▌█▌███▐███░▀\n" +
                        "▐░▐██░░▄▄▐▀█░░░▐▄█▀▌█▐███▐█\n" +
                        "░░███░▌▄█▌░░▀░░▀██░░▀██████▌\n" +
                        "░░░▀█▌▀██▀░▄░░░░░░░░░███▐███\n" +
                        "░░░░██▌░░░░░░░░░░░░░▐███████▌\n" +
                        "░░░░███░░░░░▀█▀░░░░░▐██▐███▀▌\n" +
                        "░░░░▌█▌█▄░░░░░░░░░▄▄████▀░▀\n" +
                        "░░░░░░█▀██▄▄▄░▄▄▀▀▒█▀█░▀\n" +
                        "░░░░░░░░░▀░▀█▀▌▒▒▒░▐▄▄\n" +
                        "░░░░░░░░▄▄▀▀▄░░░░░░▄▀░▀▀▄▄\n" +
                        "░░░░░░▄▀░▄▀▄░▌░░░▄▀░░░░░░▄▀▀▄\n" +
                        "░░░░░▐▒▄▀▄▀░▌▀▄▄▀░░░░░░▄▀▒▒▒▐\n" +
                        "░░░░▐▒▒▌▀▄░░░░░▌░░░░▄▄▀▒▐▒▒▒▒▌\n" +
                        "░░░▐▒▒▐░▌░▀▄░░▄▀▀▄▀▀▒▌▒▐▒▒▒▒▐▐\n" +
                        "░░░▌▄▀░░░▄▀░█▀▒▒▒▒▀▄▒▌▐▒▒▒▒▒▌▌\n" +
                        "░░▄▀▒▐░▄▀░░░▌▒▐▒▐▒▒▒▌▀▒▒▒▒▒▐▒▌\n" +
                        "██████████▌▓▓    ▒▒▒▒    ▓██████ \n" +

                        RESET
        );
    }
}