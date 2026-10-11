package com.javaguide.highconcurrency.db;

/**
 * 数据库读写分离 / 分库分表 / SQL优化 概念演示
 * <p>读写分离：主库写、从库读（binlog 同步），MyCat/ShardingSphere 实现。
 * 分库分表：垂直拆分(按业务) + 水平拆分(按 ID 取模/范围/一致性哈希)。
 * 分片键选择、扩容、分布式ID、跨库 join 问题。</p>
 */
public class DbShardingDemo {

    static String shardByMod(long id, int shardCount) {
        return "ds_" + (id % shardCount);
    }

    public static void main(String[] args) {
        System.out.println("=== 分库分表（按用户ID取模）===");
        long[] ids = {1001, 1002, 1003, 1004};
        for (long id : ids) {
            System.out.println("user " + id + " -> 数据源 " + shardByMod(id, 4));
        }

        System.out.println();
        System.out.println("=== 读写分离 ===");
        System.out.println("主库(master)写: INSERT/UPDATE/DELETE；从库(slave)读: SELECT；binlog 主从同步");
        System.out.println("ShardingSphere 可配置: 主从 + 分库分表 + 读写分离");

        System.out.println();
        System.out.println("=== SQL 优化 ===");
        System.out.println("覆盖索引 / 避免隐式转换 / 深分页(游标) / 慢查询分析(EXPLAIN)");
    }
}
