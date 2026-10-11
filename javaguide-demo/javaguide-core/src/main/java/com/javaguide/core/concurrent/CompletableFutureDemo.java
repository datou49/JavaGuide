package com.javaguide.core.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 并发 - CompletableFuture 异步编程（JavaGuide 并发重点）
 * <p>覆盖：runAsync/supplyAsync、thenApply 串行化、thenCombine 两任务组合、allOf 多任务组合、
 * exceptionally 异常感知、handle 最终处理。</p>
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // 启动异步任务 + 串行化
        CompletableFuture<Integer> step1 = CompletableFuture.supplyAsync(() -> 10, pool)
                .thenApply(x -> x * 2)     // 串行：10 -> 20
                .thenApply(x -> x + 1);    // 21

        // 两任务组合：都要完成 thenCombine / 一个完成 applyToEither
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 100, pool);
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 200, pool);
        CompletableFuture<Integer> combined = f1.thenCombine(f2, Integer::sum);

        // 异常感知
        CompletableFuture<String> risky = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new IllegalStateException("boom");
            }
            return "ok";
        }, pool).handle((res, ex) -> ex == null ? res : "fallback:" + ex.getClass().getSimpleName());

        // 多任务组合 allOf
        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, step1);

        System.out.println("串行化(10->20->21) = " + step1.get());
        System.out.println("两任务组合(100+200) = " + combined.get());
        System.out.println("异常感知 handle = " + risky.get());
        all.get(2, TimeUnit.SECONDS);
        System.out.println("allOf 多任务全部完成 = true");
        pool.shutdown();
    }
}
