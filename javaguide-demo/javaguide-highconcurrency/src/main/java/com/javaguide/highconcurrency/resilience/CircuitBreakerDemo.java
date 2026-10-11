package com.javaguide.highconcurrency.resilience;

/**
 * 服务熔断 / 降级 / 超时重试 概念演示（Sentinel / Resilience4j / Hystrix）
 * <p>熔断(Circuit Breaker)：失败率达到阈值 -> 打开断路器，快速失败，一段时间后半开尝试。
 * 降级(Fallback)：依赖不可用时返回兜底结果。
 * 超时&重试：设置超时时间，失败重试需考虑幂等，避免雪崩（连接池/线程池打满）。</p>
 */
public class CircuitBreakerDemo {

    enum State { CLOSED, OPEN, HALF_OPEN }

    static class CircuitBreaker {
        private State state = State.CLOSED;
        private int failures;
        private final int threshold = 3;

        State before() {
            if (state == State.OPEN) {
                return State.OPEN; // 快速失败，不再调用下游
            }
            return state;
        }

        void onSuccess() {
            failures = 0;
            state = State.CLOSED;
        }

        void onFailure() {
            failures++;
            if (failures >= threshold) {
                state = State.OPEN; // 熔断打开
                System.out.println(">>> 连续失败 " + threshold + " 次，断路器 OPEN（熔断）");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 熔断器 状态流转 ===");
        CircuitBreaker cb = new CircuitBreaker();
        for (int i = 1; i <= 5; i++) {
            State s = cb.before();
            if (s == State.OPEN) {
                System.out.println("请求" + i + " 被熔断，直接返回降级 fallback");
                continue;
            }
            cb.onFailure();
            System.out.println("请求" + i + " 调用下游失败");
        }

        System.out.println();
        System.out.println("半开(HALF_OPEN)后放部分请求探测，成功后关闭");
        System.out.println("超时: 设置 connect/read timeout；重试需幂等；防止线程池被打满导致雪崩");
    }
}
