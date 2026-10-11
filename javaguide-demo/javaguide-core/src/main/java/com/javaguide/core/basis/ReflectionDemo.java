package com.javaguide.core.basis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Java 基础 - 反射机制详解
 * <p>反射可以在运行时获取类的信息、调用方法、修改字段，是 Spring IoC/动态代理等的基础。</p>
 */
public class ReflectionDemo {

    private final String secret = "hello-reflect";

    public static String greet(String name) {
        return "Hi, " + name;
    }

    public static void main(String[] args) throws Exception {
        Class<?> clazz = ReflectionDemo.class;

        Method greet = clazz.getMethod("greet", String.class);
        Object result = greet.invoke(null, "JavaGuide");
        System.out.println("反射调用静态方法 greet => " + result);

        Object instance = clazz.getDeclaredConstructor().newInstance();
        Field secret = clazz.getDeclaredField("secret");
        secret.setAccessible(true);
        System.out.println("反射读取私有字段 secret => " + secret.get(instance));
        secret.set(instance, "modified");
        System.out.println("反射修改私有字段后 => " + secret.get(instance));
    }
}
