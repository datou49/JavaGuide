package com.javaguide.distributed;

import com.javaguide.distributed.CapDemo;
import com.javaguide.distributed.config.NacosDemo;
import com.javaguide.distributed.gateway.GatewayDemo;
import com.javaguide.distributed.id.DistributedIdDemo;
import com.javaguide.distributed.lock.DistributedLockDemo;
import com.javaguide.distributed.tx.DistributedTxDemo;

/**
 * javaguide-distributed 统一入口。
 */
public class DistributedConsole {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== CAP / BASE / Raft ==========");
        CapDemo.main(args);
        System.out.println();
        System.out.println("========== 分布式锁 ==========");
        DistributedLockDemo.main(args);
        System.out.println();
        System.out.println("========== 分布式ID ==========");
        DistributedIdDemo.main(args);
        System.out.println();
        System.out.println("========== 分布式事务 ==========");
        DistributedTxDemo.main(args);
        System.out.println();
        System.out.println("========== 注册中心 / 配置中心 ==========");
        NacosDemo.main(args);
        System.out.println();
        System.out.println("========== API 网关 ==========");
        GatewayDemo.main(args);
    }
}
