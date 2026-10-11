package com.javaguide.highconcurrency;

import com.javaguide.highconcurrency.cache.BloomFilterDemo;
import com.javaguide.highconcurrency.ratelimit.RateLimitDemo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * javaguide-highconcurrency 冒烟测试。
 */
class HighConcurrencySmokeTest {

    @Test
    void bloomFilter_noFalseNegative() {
        var bf = new BloomFilterDemo.BloomFilter();
        bf.add("k1");
        bf.add("k2");
        assertTrue(bf.mightContain("k1"));
        assertTrue(bf.mightContain("k2"));
        assertFalse(bf.mightContain("not-exist"));
    }

    @Test
    void tokenBucket_deniesWhenExhausted() {
        var bucket = new RateLimitDemo.TokenBucket(1, 1); // 每秒1个，容量1
        assertTrue(bucket.tryAcquire());
        assertFalse(bucket.tryAcquire());
    }
}
