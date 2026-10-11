package com.javaguide.spring.web;

import com.javaguide.spring.aop.LogAspect;
import com.javaguide.spring.entity.User;
import com.javaguide.spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * RESTful 示例 + 参数校验 + 全局异常演示。
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    private final UserService userService;
    private final ApplicationContext context;

    @Value("${javaguide.demo.name:default}")
    private String demoName;

    public DemoController(UserService userService, ApplicationContext context) {
        this.userService = userService;
        this.context = context;
    }

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        return Map.of("message", "Hello, JavaGuide!", "demoName", demoName);
    }

    @GetMapping("/ioc/beans")
    public Map<String, Object> iocBeans() {
        return Map.of(
                "userService", userService.getClass().getName(),
                "isProxy(CGLIB/JDK动态代理)", context.getBean(UserService.class).getClass().getName(),
                "logAspect", context.getBean(LogAspect.class).getClass().getName(),
                "container", "ApplicationContext(IoC容器)"
        );
    }

    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/order/pay")
    public Map<String, Object> pay(@RequestBody Map<String, Object> body) {
        boolean fail = Boolean.TRUE.equals(body.get("fail"));
        long before = userService.count();
        userService.transferDemo(fail);
        long after = userService.count();
        return Map.of(
                "fail", fail,
                "事务前条数", before,
                "事务后条数", after,
                "是否回滚", after == before
        );
    }

    @GetMapping("/auto-config")
    public Map<String, Object> autoConfig() {
        return Map.of(
                "说明", "SpringBoot 自动装配 = @EnableAutoConfiguration + META-INF/spring.factories 加载配置类",
                "webServer", context.getEnvironment().getProperty("server.port", "8080"),
                "启动类包扫描", "com.javaguide.spring"
        );
    }
}
