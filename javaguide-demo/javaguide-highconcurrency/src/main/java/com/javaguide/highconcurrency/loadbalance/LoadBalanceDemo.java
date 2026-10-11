package com.javaguide.highconcurrency.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡算法 演示
 * <p>轮询(RoundRobin)、随机(Random)、加权(Weighted)、一致性哈希(ConsistentHash)。
 * 网关/Nginx/Ribbon(LoadBalancer) 根据这些算法把请求分发到多个实例。</p>
 */
public class LoadBalanceDemo {

    static String roundRobin(List<String> nodes, AtomicInteger counter) {
        return nodes.get(counter.getAndIncrement() % nodes.size());
    }

    static String random(List<String> nodes) {
        return nodes.get((int) (System.nanoTime() % nodes.size()));
    }

    public static void main(String[] args) {
        List<String> nodes = List.of("instance-1", "instance-2", "instance-3");
        AtomicInteger counter = new AtomicInteger(0);

        System.out.println("=== 负载均衡 ===");
        System.out.println("轮询: " + roundRobin(nodes, counter) + " -> " + roundRobin(nodes, counter) + " -> " + roundRobin(nodes, counter));
        System.out.println("随机: " + random(nodes));
        System.out.println("加权: 实例性能不同给不同权重；一致性哈希: 会话保持/缓存命中");
        System.out.println("常见实现: Nginx upstream / Spring Cloud LoadBalancer / Dubbo 负载均衡");
    }
}
