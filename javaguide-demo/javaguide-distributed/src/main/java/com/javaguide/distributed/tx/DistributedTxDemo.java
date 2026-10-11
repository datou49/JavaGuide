package com.javaguide.distributed.tx;

/**
 * 分布式事务 - 解决方案演示
 * <p>本地事务失效场景：多服务/多库。常见方案：
 * <ul>
 *   <li>2PC/XA：两阶段提交，强一致，性能低</li>
 *   <li>TCC：Try-Confirm-Cancel，业务侵入大</li>
 *   <li>本地消息表 + 消息队列：最终一致，主流</li>
 *   <li>Seata：AT 模式（自动补偿，推荐入门）；TCC 模式</li>
 * </ul>
 * Seata 核心：TC(事务协调器) + TM(事务管理器) + RM(资源管理器)，全局事务 XID 透传。</p>
 */
public class DistributedTxDemo {

    public static void main(String[] args) {
        System.out.println("=== 分布式事务 常见解决方案 ===");
        System.out.println("1) 2PC/XA    强一致、实现简单、性能差、阻塞");
        System.out.println("2) TCC       灵活、业务侵入强（Try/Confirm/Cancel）");
        System.out.println("3) 本地消息表 + MQ  最终一致、解耦（订单->扣库存）");
        System.out.println("4) Seata AT  自动补偿，业务无侵入（本项目 gulimall 用的方案）");

        System.out.println();
        System.out.println("Seata 角色：TC 协调器 / TM 事务管理器 / RM 资源管理器");
        System.out.println("全局事务：TM 开启全局事务 -> 各 RM 分支事务 -> TC 提交/回滚 + 反向 SQL 补偿");
    }
}
