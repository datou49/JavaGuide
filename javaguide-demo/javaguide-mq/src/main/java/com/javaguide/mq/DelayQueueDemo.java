package com.javaguide.mq;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ - 延时队列（基于 TTL + 死信队列）模拟
 * <p>思路：消息先进入「TTL 队列」，TTL 到期且无消费者消费 -> 进入死信交换机(DLX) -> 路由到真正处理队列。
 * 本示例用 JDK DelayQueue 模拟延时关单（谷粒商城订单超时关闭/库存解锁）。</p>
 */
public class DelayQueueDemo {

    static class OrderMessage implements Delayed {
        final String orderId;
        final long expireAt;

        OrderMessage(String orderId, long delayMs) {
            this.orderId = orderId;
            this.expireAt = System.currentTimeMillis() + delayMs;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expireAt - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.expireAt, ((OrderMessage) o).expireAt);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<OrderMessage> queue = new DelayQueue<>();
        queue.put(new OrderMessage("ORDER-001", 800));
        queue.put(new OrderMessage("ORDER-002", 300));
        queue.put(new OrderMessage("ORDER-003", 1200));

        System.out.println("=== 延时队列（模拟 TTL 死信后关单）===");
        long start = System.currentTimeMillis();
        while (!queue.isEmpty()) {
            OrderMessage msg = queue.take();
            System.out.printf("[%3dms] 订单 %s 超时关闭%n",
                    System.currentTimeMillis() - start, msg.orderId);
        }
        System.out.println("RabbitMQ 实现：queue TTL(如30min) + x-dead-letter-exchange 到处理队列");
    }
}
