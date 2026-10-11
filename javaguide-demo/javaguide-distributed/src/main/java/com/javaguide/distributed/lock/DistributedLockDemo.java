package com.javaguide.distributed.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 分布式锁 - 实现方案（内存模拟 Redis SETNX + Lua）
 * <p>方案：
 * <ul>
 *   <li>Redis：SET key value NX PX 30000（原子加锁+过期） -> Lua 脚本原子解锁（比对 value 再删，防误删）</li>
 *   <li>Redisson：看门狗自动续期，解决死锁；可重入、公平锁、读写锁、信号量、闭锁</li>
 *   <li>ZooKeeper：临时顺序节点 + 监听，强一致</li>
 *   <li>数据库：唯一索引/乐观锁（性能低，一般不选）</li>
 * </ul>
 * 本示例模拟「Redis 分布式锁」的获取/释放语义。</p>
 */
public class DistributedLockDemo {

    private static final ConcurrentHashMap<String, String> locks = new ConcurrentHashMap<>();

    public static boolean tryLock(String key, String owner, long ttlMs) {
        boolean got = locks.putIfAbsent(key, owner) == null;
        if (got) {
            // 模拟过期时间：新线程清理
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(ttlMs);
                } catch (InterruptedException ignored) {
                }
                locks.remove(key, owner);
            }).start();
        }
        return got;
    }

    public static void unlock(String key, String owner) {
        // Lua 原子：if get(key)==owner then del(key)
        locks.remove(key, owner);
    }

    public static void main(String[] args) throws InterruptedException {
        String key = "stock:1001";
        String owner1 = "node-A";
        String owner2 = "node-B";

        System.out.println("=== Redis 分布式锁（SETNX + 过期 + Lua 解锁）===");
        boolean l1 = tryLock(key, owner1, 2000);
        System.out.println("node-A 获取锁: " + l1);
        boolean l2 = tryLock(key, owner2, 2000);
        System.out.println("node-B 尝试获取(应失败): " + l2);

        unlock(key, owner1);
        System.out.println("node-A 正常释放锁");
        boolean l3 = tryLock(key, owner2, 2000);
        System.out.println("node-B 再次获取(应成功): " + l3);

        System.out.println();
        System.out.println("Redisson 扩展：watchdog 看门狗每 10s 续期防止死锁；可重入/公平/读写锁/信号量");
    }
}
