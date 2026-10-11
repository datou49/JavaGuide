package com.javaguide.core.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * 集合 - HashMap 核心原理演示
 * <p>JDK8+ 底层：数组 + 链表 + 红黑树；默认容量 16、负载因子 0.75；
 * 扩容阈值 = 容量 * 负载因子；树化阈值 8（链表长度），退化阈值 6。</p>
 */
public class HashMapDemo {

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>(16);
        map.put("java", 1);
        map.put("guide", 2);
        map.put("java", 3); // 覆盖

        System.out.println("size = " + map.size());
        System.out.println("get(java) = " + map.get("java"));

        int capacity = 16;
        float factor = 0.75f;
        System.out.println("初始阈值 threshold = " + (int) (capacity * factor) + "（容量*负载因子，超过即扩容）");

        for (int i = 0; i < 100; i++) {
            map.put("key-" + i, i);
        }
        System.out.println("扩容/插入 100 条后 size = " + map.size());
        System.out.println("HashMap 非线程安全；并发写会丢失数据，需用 ConcurrentHashMap。");
    }
}
