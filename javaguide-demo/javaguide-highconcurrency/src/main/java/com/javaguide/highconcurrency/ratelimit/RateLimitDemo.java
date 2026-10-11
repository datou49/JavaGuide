package com.javaguide.highconcurrency.ratelimit;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 限流算法演示
 * <p>常见限流算法：
 * <ul>
 *   <li>固定窗口：单位时间窗口内计数，超过阈值拒绝；边界突刺问题</li>
 *   <li>滑动窗口：细分窗口，平滑</li>
 *   <li>漏桶：恒定速率流出，突发被削平</li>
 *   <li>令牌桶：按速率放入令牌，可取令牌则通过，允许一定突发</li>
 * </ul>
 * 生产：Guava RateLimiter(令牌桶)、Sentinel 流控。</p>
 */
public class RateLimitDemo {

    /** 令牌桶（简易实现） */
    public static class TokenBucket {
        private final double rate;    // 每秒生成令牌数
        private final long capacity;  // 桶容量
        private double tokens;
        private long lastRefill;

        public TokenBucket(double rate, long capacity) {
            this.rate = rate;
            this.capacity = capacity;
            this.tokens = capacity;
            this.lastRefill = System.nanoTime();
        }

        public synchronized boolean tryAcquire() {
            long now = System.nanoTime();
            tokens = Math.min(capacity, tokens + (now - lastRefill) / 1_000_000_000.0 * rate);
            lastRefill = now;
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 令牌桶限流（rate=10/s, capacity=5）===");
        TokenBucket bucket = new TokenBucket(10, 5);
        int pass = 0, deny = 0;
        for (int i = 0; i < 20; i++) {
            if (bucket.tryAcquire()) {
                pass++;
            } else {
                deny++;
            }
        }
        System.out.println("前 20 个请求：放行=" + pass + "，拒绝=" + deny + "（桶容量 5 + 短暂补充）");

        System.out.println();
        System.out.println("固定窗口 vs 滑动窗口 vs 漏桶 vs 令牌桶 概念对比");
        System.out.println("Sentinel 流控：QPS 阈值、并发线程数；漏桶削峰 / 令牌桶允许突发");
    }
}
