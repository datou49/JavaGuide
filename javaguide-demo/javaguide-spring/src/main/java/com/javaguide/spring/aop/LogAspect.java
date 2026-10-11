package com.javaguide.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Spring - AOP 切面示例
 * <p>通过 @Before/@After/@Around 增强业务方法。默认 JDK 动态代理(接口) 或 CGLIB(类)。
 * 典型应用：日志、权限校验、性能监控、分布式事务、缓存。</p>
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.javaguide.spring.service..*(..))")
    public void serviceLayer() {
    }

    @Before("serviceLayer()")
    public void before() {
        log.info("[AOP-Before] 方法执行前：记录日志/校验权限");
    }

    @AfterReturning(pointcut = "serviceLayer()", returning = "ret")
    public void afterReturning(Object ret) {
        log.info("[AOP-AfterReturning] 方法正常返回，结果={}", ret);
    }

    @After("serviceLayer()")
    public void after() {
        log.info("[AOP-After] 方法执行结束（finally 语义）");
    }

    @Around("serviceLayer()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            log.info("[AOP-Around] {}.{}() 耗时 {}ms", pjp.getSignature().getDeclaringType().getSimpleName(),
                    pjp.getSignature().getName(), System.currentTimeMillis() - start);
            return result;
        } catch (Throwable t) {
            log.warn("[AOP-Around] 方法异常: {}", t.getMessage());
            throw t;
        }
    }
}
