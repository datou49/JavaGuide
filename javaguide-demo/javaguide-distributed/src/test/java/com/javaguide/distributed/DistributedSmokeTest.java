package com.javaguide.distributed;

import com.javaguide.distributed.id.DistributedIdDemo;
import com.javaguide.distributed.lock.DistributedLockDemo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * javaguide-distributed 冒烟测试。
 */
class DistributedSmokeTest {

    @Test
    void lock_shouldBeExclusive() {
        String key = "test-key";
        boolean l1 = DistributedLockDemo.tryLock(key, "A", 500);
        boolean l2 = DistributedLockDemo.tryLock(key, "B", 500);
        assertTrue(l1);
        assertFalse(l2);
        DistributedLockDemo.unlock(key, "A");
        boolean l3 = DistributedLockDemo.tryLock(key, "B", 500);
        assertTrue(l3);
        DistributedLockDemo.unlock(key, "B");
    }

    @Test
    void snowflake_shouldIncrease() {
        long id1 = DistributedIdDemo.snowflake(1, 1);
        long id2 = DistributedIdDemo.snowflake(1, 1);
        assertNotEquals(id1, id2);
        assertTrue(id2 > id1);
    }
}
