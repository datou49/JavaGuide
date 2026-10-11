package com.javaguide.highconcurrency.cache;

import java.util.BitSet;

/**
 * 布隆过滤器（Bloom Filter）实现
 * <p>用于缓存穿透防护、海量数据去重等。原理：多个哈希函数映射到位数组，
 * 可能存在误判(说存在实际不存在)，但绝不可能漏报。本示例用 BitSet 实现简化版。</p>
 */
public class BloomFilterDemo {

    public static class BloomFilter {
        private final BitSet bits = new BitSet(1 << 20);
        private final int[] seeds = {3, 7, 11, 19, 43};

        private int hash(String s, int seed) {
            int h = 0;
            for (char c : s.toCharArray()) {
                h = h * seed + c;
            }
            return Math.abs(h) % bits.size();
        }

        public void add(String s) {
            for (int seed : seeds) {
                bits.set(hash(s, seed));
            }
        }

        public boolean mightContain(String s) {
            for (int seed : seeds) {
                if (!bits.get(hash(s, seed))) {
                    return false; // 只要一个位为 0，则肯定不存在
                }
            }
            return true; // 可能误判
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 布隆过滤器（缓存穿透防护）===");
        BloomFilter bf = new BloomFilter();
        bf.add("user:1001");
        bf.add("user:1002");

        System.out.println("user:1001 存在? " + bf.mightContain("user:1001") + "（true=可能存在，可放行查缓存/DB）");
        System.out.println("user:9999 存在? " + bf.mightContain("user:9999") + "（false=一定不存在，直接拦截，防穿透）");
        System.out.println("结论：不存在时绝不误报，存在时可能误报（false positive）");
    }
}
