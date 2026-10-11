package com.javaguide.mq;

/**
 * RabbitMQ - Exchange 类型 与 路由演示（内存模拟）
 * <p>四种 Exchange：
 * <ul>
 *   <li>Direct：按 routingKey 精确匹配</li>
 *   <li>Fanout：广播，忽略 routingKey，绑定即收到</li>
 *   <li>Topic：routingKey 通配符匹配（* 一个词，# 零个或多个词）</li>
 *   <li>Headers：按 header 匹配（不常用）</li>
 * </ul>
 */
public class ExchangeDemo {

    static String direct(String routingKey, String wanted) {
        return routingKey.equals(wanted) ? "投递到队列" : "不投递";
    }

    static boolean topic(String pattern, String routingKey) {
        String[] p = pattern.split("\\.");
        String[] r = routingKey.split("\\.");
        int pi = 0;
        int ri = 0;
        while (pi < p.length && ri < r.length) {
            if (p[pi].equals("#")) {
                // # 匹配剩余所有词（含零个）：尝试直接结束或跳过路由词
                if (pi == p.length - 1) {
                    return true;
                }
                // 尝试让 # 匹配 0..n 个词，递归
                for (int skip = 0; skip <= r.length - ri; skip++) {
                    if (topic(join(p, pi + 1), join(r, ri + skip))) {
                        return true;
                    }
                }
                return false;
            }
            if (p[pi].equals("*") || p[pi].equals(r[ri])) {
                pi++;
                ri++;
            } else {
                return false;
            }
        }
        // 两边都必须耗尽；剩余 pattern 只能是 #（可匹配零词）
        while (pi < p.length && p[pi].equals("#")) {
            pi++;
        }
        return pi == p.length && ri == r.length;
    }

    private static String join(String[] arr, int from) {
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < arr.length; i++) {
            if (sb.length() > 0) {
                sb.append('.');
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Exchange 类型 ===");
        System.out.println("Direct: routingKey=order.create, 绑定=order.create => " + direct("order.create", "order.create"));
        System.out.println("Direct: routingKey=order.create, 绑定=order.pay   => " + direct("order.create", "order.pay"));

        System.out.println("Fanout: 广播给所有绑定队列（无论 routingKey）");
        System.out.println("Topic: pattern=order.*  , routingKey=order.create => " + topic("order.*", "order.create"));
        System.out.println("Topic: pattern=order.*  , routingKey=order.pay.cancel => " + topic("order.*", "order.pay.cancel"));
        System.out.println("Topic: pattern=order.#  , routingKey=order.pay.cancel => " + topic("order.#", "order.pay.cancel"));
        System.out.println("Topic: pattern=*.pay   , routingKey=order.pay     => " + topic("*.pay", "order.pay"));
    }
}
