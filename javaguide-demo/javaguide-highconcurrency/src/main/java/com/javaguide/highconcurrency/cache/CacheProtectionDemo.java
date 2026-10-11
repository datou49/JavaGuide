package com.javaguide.highconcurrency.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存三兄弟：穿透 / 击穿 / 雪崩 及解决（内存模拟）
 * <p>
 * 穿透：查询不存在的数据，绕过缓存直击 DB -> 布隆过滤器 / 空值缓存。
 * 击穿：热点 key 过期瞬间大量请求打到 DB -> 互斥锁/分布式锁重建缓存。
 * 雪崩：大量 key 同时过期 / 服务宕机 -> 过期时间加随机值 / 多级缓存 / 高可用。
 * </p>
 */
public class CacheProtectionDemo {

    static class CacheWithMutex {
        private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, Boolean> building = new ConcurrentHashMap<>();

        String getOrBuild(String key, String dbValue) throws InterruptedException {
            String hit = cache.get(key);
            if (hit != null) {
                return hit;
            }
            // 互斥锁：只有一个线程重建缓存，其余等待
            Boolean owner = building.putIfAbsent(key, true);
            if (owner == null) {
                try {
                    Thread.sleep(30); // 模拟查询 DB
                    cache.put(key, dbValue);
                    return dbValue;
                } finally {
                    building.remove(key);
                }
            } else {
                // 其它线程等待后重查缓存
                for (int i = 0; i < 10 && cache.get(key) == null; i++) {
                    Thread.sleep(20);
                }
                return cache.get(key);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 缓存击穿：热点 key 过期，互斥锁重建 ===");
        CacheWithMutex cache = new CacheWithMutex();
        int threads = 5;
        var latch = new java.util.concurrent.CountDownLatch(threads);
        var pool = java.util.concurrent.Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    System.out.println("  得到 value=" + cache.getOrBuild("hot-key", "DB-数据"));
                } catch (InterruptedException ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        pool.shutdown();
        System.out.println("5 个并发请求全部拿到缓存（只有 1 次真正查 DB）");

        System.out.println();
        System.out.println("穿透: 布隆过滤器拦截不存在的 key / 空值缓存");
        System.out.println("雪崩: TTL 加随机值(e.g. + 0~300s)、多级缓存(本地+Redis)、集群高可用");
    }
}
