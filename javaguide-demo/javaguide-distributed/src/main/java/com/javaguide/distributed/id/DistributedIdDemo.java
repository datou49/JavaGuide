package com.javaguide.distributed.id;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 分布式ID - 方案演示
 * <p>常见方案：UUID、数据库自增/号段、Redis INCR、雪花算法(Snowflake)、美团Leaf(号段/雪花)。
 * 雪花算法：41位时间戳 + 10位机器ID + 12位序列号，趋势递增、去中心化。</p>
 */
public class DistributedIdDemo {

    private static final AtomicLong seq = new AtomicLong(0);
    private static final long START_EPOCH = 1700000000000L;

    /** 简易雪花算法实现 */
    public static long snowflake(int workerId, int dataCenterId) {
        long timestamp = System.currentTimeMillis();
        long id = ((timestamp - START_EPOCH) << 22)
                | ((long) dataCenterId << 17)
                | ((long) workerId << 12)
                | (seq.incrementAndGet() & 0xFFF);
        return id;
    }

    public static void main(String[] args) {
        System.out.println("=== 分布式ID 方案 ===");
        System.out.println("UUID:   " + UUID.randomUUID());
        System.out.println("雪花:   " + snowflake(1, 1));
        System.out.println("雪花:   " + snowflake(1, 1) + "（趋势递增）");
        System.out.println("其他: 数据库号段 / Redis INCR / Leaf 号段");
    }
}
