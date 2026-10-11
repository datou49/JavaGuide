package com.javaguide.mq;

/**
 * javaguide-mq 统一入口。
 */
public class MqConsole {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== Exchange 类型 ==========");
        ExchangeDemo.main(args);
        System.out.println();
        System.out.println("========== 延时队列（TTL+死信）==========");
        DelayQueueDemo.main(args);
        System.out.println();
        System.out.println("========== 可靠投递 / 丢失重复积压 ==========");
        ReliableDeliveryDemo.main(args);
    }
}
