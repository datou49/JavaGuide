package com.javaguide.mq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * javaguide-mq 冒烟测试。
 */
class MqSmokeTest {

    @Test
    void directRouting_works() {
        assertEquals("投递到队列", ExchangeDemo.direct("order.create", "order.create"));
        assertEquals("不投递", ExchangeDemo.direct("order.create", "order.pay"));
    }

    @Test
    void topicMatching_works() {
        assertTrue(ExchangeDemo.topic("order.*", "order.create"));
        assertFalse(ExchangeDemo.topic("order.*", "order.pay.cancel"));
        assertTrue(ExchangeDemo.topic("order.#", "order.pay.cancel"));
        assertTrue(ExchangeDemo.topic("*.pay", "order.pay"));
    }

    @Test
    void delayQueue_orders() throws InterruptedException {
        var q = new java.util.concurrent.DelayQueue<DelayQueueDemo.OrderMessage>();
        q.put(new DelayQueueDemo.OrderMessage("A", 500));
        q.put(new DelayQueueDemo.OrderMessage("B", 100));
        var first = q.take();
        assertEquals("B", first.orderId); // 先到期的先出队
        var second = q.take();
        assertEquals("A", second.orderId);
    }
}
