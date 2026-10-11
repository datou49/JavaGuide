package com.javaguide.core;

import com.javaguide.core.basis.ProxyDemo;
import com.javaguide.core.basis.ReflectionDemo;
import com.javaguide.core.basis.ValuePassingDemo;
import com.javaguide.core.collection.ConcurrentHashMapDemo;
import com.javaguide.core.collection.HashMapDemo;
import com.javaguide.core.concurrent.AqsDemo;
import com.javaguide.core.concurrent.CasDemo;
import com.javaguide.core.concurrent.CompletableFutureDemo;
import com.javaguide.core.concurrent.ReentrantLockDemo;
import com.javaguide.core.concurrent.ThreadLocalDemo;
import com.javaguide.core.concurrent.ThreadPoolDemo;
import com.javaguide.core.concurrent.VirtualThreadDemo;
import com.javaguide.core.jvm.ClassLoadingDemo;
import com.javaguide.core.jvm.GcDemo;
import com.javaguide.core.jvm.MemoryAreaDemo;

/**
 * javaguide-core 统一控制台入口：依次运行各知识点 demo。
 */
public class ConsoleDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("========== Java 基础 ==========");
        ReflectionDemo.main(args);
        System.out.println();
        ProxyDemo.main(args);
        System.out.println();
        ValuePassingDemo.main(args);
        System.out.println();

        System.out.println("========== 集合 ==========");
        HashMapDemo.main(args);
        System.out.println();
        ConcurrentHashMapDemo.main(args);
        System.out.println();

        System.out.println("========== 并发 ==========");
        ThreadPoolDemo.main(args);
        System.out.println();
        ReentrantLockDemo.main(args);
        System.out.println();
        CasDemo.main(args);
        System.out.println();
        AqsDemo.main(args);
        System.out.println();
        ThreadLocalDemo.main(args);
        System.out.println();
        CompletableFutureDemo.main(args);
        System.out.println();
        VirtualThreadDemo.main(args);
        System.out.println();

        System.out.println("========== JVM ==========");
        MemoryAreaDemo.main(args);
        System.out.println();
        GcDemo.main(args);
        System.out.println();
        ClassLoadingDemo.main(args);
    }
}
