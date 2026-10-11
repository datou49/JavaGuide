package com.javaguide.highconcurrency;

import com.javaguide.highconcurrency.cache.BloomFilterDemo;
import com.javaguide.highconcurrency.cache.CacheProtectionDemo;
import com.javaguide.highconcurrency.db.DbShardingDemo;
import com.javaguide.highconcurrency.loadbalance.LoadBalanceDemo;
import com.javaguide.highconcurrency.pool.ThreadPoolIsolationDemo;
import com.javaguide.highconcurrency.ratelimit.RateLimitDemo;
import com.javaguide.highconcurrency.resilience.CircuitBreakerDemo;

/**
 * javaguide-highconcurrency 统一入口。
 */
public class HighConcurrencyConsole {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== 限流 ==========");
        RateLimitDemo.main(args);
        System.out.println();
        System.out.println("========== 熔断/降级/超时重试 ==========");
        CircuitBreakerDemo.main(args);
        System.out.println();
        System.out.println("========== 缓存穿透/击穿/雪崩 ==========");
        CacheProtectionDemo.main(args);
        System.out.println();
        System.out.println("========== 布隆过滤器 ==========");
        BloomFilterDemo.main(args);
        System.out.println();
        System.out.println("========== 线程池隔离 ==========");
        ThreadPoolIsolationDemo.main(args);
        System.out.println();
        System.out.println("========== 负载均衡 ==========");
        LoadBalanceDemo.main(args);
        System.out.println();
        System.out.println("========== 读写分离/分库分表 ==========");
        DbShardingDemo.main(args);
    }
}
