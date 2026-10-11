package com.javaguide.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Spring - IoC 配置类
 * <p>@Configuration 等价于 XML 配置；@Bean 将方法返回值注册为容器 Bean。
 * 默认单例(singleton)，也可 prototype 等。</p>
 */
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean("asyncPool")
    public ExecutorService asyncPool() {
        return Executors.newFixedThreadPool(2);
    }
}
