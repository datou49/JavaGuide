package com.javaguide.mq;

import java.util.UUID;

/**
 * 消息队列 - 可靠投递、消息丢失/重复/积压 概念与对策
 * <p>可靠投递三步：
 * 1) 发送端确认(Confirm)：broker 收到消息后回调 ack；
 * 2) 消息持久化：exchange/queue/message durable；
 * 3) 消费端确认(ACK) + 失败重试/死信。
 * 重复消费 -> 幂等（唯一ID/数据库唯一键）；消息丢失 -> Confirm+持久化+ACK；积压 -> 消费者扩容/死信补偿。</p>
 */
public class ReliableDeliveryDemo {

    public static void main(String[] args) {
        System.out.println("=== 可靠投递 ===");
        String confirmId = UUID.randomUUID().toString();
        System.out.println("1) 发送端 Confirm: 消息[" + confirmId + "] 发送，等待 broker ack");
        System.out.println("   publisher-confirm-type: correlated -> 收到 ack 视为投递成功");
        System.out.println("2) 持久化: exchange/queue/message 全部 durable=true，重启不丢");
        System.out.println("3) 消费端 ACK: 手动 ack，处理失败 -> basicNack + requeue/死信");

        System.out.println();
        System.out.println("=== 消息丢失/重复/积压 ===");
        System.out.println("丢失:  生产端 Confirm 未 ack -> 重发；broker 持久化；消费端手动 ack");
        System.out.println("重复:  消费端做幂等（业务唯一ID + 数据库唯一索引），重复消息丢弃");
        System.out.println("积压:  消费者消费慢 -> 增加消费者/临时扩容 + 死信队列兜底");
    }
}
