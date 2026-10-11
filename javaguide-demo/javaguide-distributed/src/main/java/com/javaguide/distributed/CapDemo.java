package com.javaguide.distributed;

/**
 * CAP / BASE / Raft / 一致性哈希 概念演示
 * <p>CAP：一致性(Consistency)、可用性(Availability)、分区容错(Partition tolerance) 三者最多取其二。
 * BASE：基本可用(Basically Available) + 软状态(Soft state) + 最终一致(Eventually consistent)。
 * Raft：日志复制一致性算法（选主/复制/安全性），Nacos 等使用。
 * 一致性哈希：解决节点增减时大量数据迁移的哈希环。</p>
 */
public class CapDemo {

    public static void main(String[] args) {
        System.out.println("=== CAP 理论 ===");
        System.out.println("一致性 C / 可用性 A / 分区容错 P，网络分区时只能 C 或 A");
        System.out.println("CP: ZooKeeper / etcd（保证一致，分区时不可用）");
        System.out.println("AP: Eureka / Cassandra（保证可用，最终一致）");
        System.out.println("CA: 单机数据库（无分区）");

        System.out.println();
        System.out.println("=== BASE 理论 ===");
        System.out.println("基本可用 + 软状态 + 最终一致（分布式事务的柔性方案基础）");

        System.out.println();
        System.out.println("=== Raft 算法 ===");
        System.out.println("角色: Leader / Follower / Candidate；日志复制 + 心跳 + 选主；半数以上提交");

        System.out.println();
        System.out.println("=== 一致性哈希 ===");
        System.out.println("哈希环 + 虚拟节点：节点增删只影响相邻节点，减少数据迁移");
    }
}
