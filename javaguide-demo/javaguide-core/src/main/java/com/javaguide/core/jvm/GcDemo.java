package com.javaguide.core.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * JVM - 垃圾回收（GC）与堆内存演示
 * <p>年轻代(Eden + 两个 Survivor)用复制算法，老年代用标记整理/标记清除；GC Roots 可达性分析。
 * 本示例演示对象晋升与 GC 行为观察（配合 -Xlog:gc 可看日志）。</p>
 */
public class GcDemo {

    public static void main(String[] args) {
        System.out.println("JVM 默认垃圾回收器: "
                + java.lang.management.ManagementFactory.getGarbageCollectorMXBeans()
                .stream().map(g -> g.getName()).reduce((a, b) -> a + ", " + b).orElse("none"));

        // 制造大量短命对象触发 Minor GC
        List<byte[]> keep = new ArrayList<>();
        for (int round = 0; round < 5; round++) {
            for (int i = 0; i < 100_000; i++) {
                byte[] b = new byte[1024];
                if (i % 20 == 0) {
                    keep.add(b); // 少量对象被保留，其余成为垃圾
                }
            }
            System.out.println("round " + round + " 结束，已保留对象数=" + keep.size());
        }
    }
}
