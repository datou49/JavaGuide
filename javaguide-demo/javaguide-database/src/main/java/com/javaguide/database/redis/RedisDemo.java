package com.javaguide.database.redis;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Redis - 5 种基本数据结构 + 缓存读写策略 + 缓存穿透/击穿/雪崩
 * <p>用内存 Map 模拟 Redis 数据结构；重点演示 3 种缓存读写策略（Cache Aside / Read Through / Write Through）。
 * Redis 常用：String/Hash/List/Set/ZSet，以及 Bitmap/HyperLogLog/Geo 特殊结构。</p>
 */
public class RedisDemo {

    private final Map<String, Object> store = new HashMap<>();

    public void set(String k, Object v) {
        store.put(k, v);
    }

    public Object get(String k) {
        return store.get(k);
    }

    public static void main(String[] args) {
        RedisDemo cache = new RedisDemo();
        RedisDemo db = new RedisDemo();
        db.set("user:1", "JavaGuide");

        System.out.println("=== Redis 5 种基本数据结构 ===");
        String[] structs = {"String(字符串)", "Hash(哈希)", "List(列表)", "Set(集合)", "ZSet(有序集合)"};
        System.out.println(String.join(" / ", structs));

        System.out.println();
        System.out.println("=== Cache Aside（旁路缓存）===");
        Object v = cache.get("user:1");
        if (v == null) {
            v = db.get("user:1"); // 缓存未命中，回源数据库
            cache.set("user:1", v); // 回填缓存
            System.out.println("缓存未命中 -> 回源 DB 并回填，value=" + v);
        } else {
            System.out.println("缓存命中 value=" + v);
        }

        System.out.println();
        System.out.println("=== 缓存三兄弟 ===");
        System.out.println("穿透: 查询不存在 key，DB 也无 -> 用布隆过滤器/空值缓存");
        System.out.println("击穿: 热点 key 过期 -> 用互斥锁重建缓存");
        System.out.println("雪崩: 大量 key 同时过期 -> 过期时间加随机值/多级缓存");

        System.out.println();
        System.out.println("=== LRU 缓存（LinkedHashMap 模拟）===");
        Map<String, String> lru = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 3;
            }
        };
        lru.put("a", "1");
        lru.put("b", "2");
        lru.put("c", "3");
        lru.get("a"); // 访问 a 使其成为最近使用
        lru.put("d", "4");
        System.out.println("容量3，访问a后插入d，淘汰最久未使用的b => " + lru.keySet());
    }
}
