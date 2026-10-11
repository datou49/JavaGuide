package com.javaguide.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * javaguide-spring 启动类。
 * <p>演示：IoC 容器、AOP 切面、@Transactional 事务、SpringBoot 自动装配、参数校验、全局异常、RESTful。</p>
 * 访问：
 * <ul>
 *   <li>GET  /api/hello                -> 普通接口</li>
 *   <li>GET  /api/ioc/beans            -> 查看容器中 Bean</li>
 *   <li>POST /api/user                 -> 参数校验 + 全局异常</li>
 *   <li>POST /api/order/pay            -> 事务回滚演示</li>
 *   <li>GET  /api/auto-config          -> 自动装配示例</li>
 * </ul>
 */
@SpringBootApplication
public class SpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }
}
