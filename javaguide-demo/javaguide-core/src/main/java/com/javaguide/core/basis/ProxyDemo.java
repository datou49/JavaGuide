package com.javaguide.core.basis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Java 基础 - 代理模式详解（JDK 动态代理）
 * <p>JDK 动态代理基于接口，通过 Proxy + InvocationHandler 在运行时生成代理类。
 * Spring AOP 默认使用 JDK 动态代理（针对接口），或 CGLIB（针对类）。</p>
 */
public class ProxyDemo {

    public interface Say {
        String say(String name);
    }

    public static class RealSay implements Say {
        @Override
        public String say(String name) {
            return "RealSay: " + name;
        }
    }

    public static void main(String[] args) {
        Say real = new RealSay();
        Say proxy = (Say) Proxy.newProxyInstance(
                Say.class.getClassLoader(),
                new Class<?>[]{Say.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxyObj, Method method, Object[] args) throws Throwable {
                        System.out.println("[Proxy] before: " + method.getName());
                        Object result = method.invoke(real, args);
                        System.out.println("[Proxy] after, result=" + result);
                        return result;
                    }
                });

        System.out.println("代理类是否实现接口: " + (proxy instanceof Say));
        System.out.println("通过代理调用 => " + proxy.say("JavaGuide"));
    }
}
