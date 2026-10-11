package com.javaguide.database;

import com.javaguide.database.es.EsDemo;
import com.javaguide.database.mysql.MySqlDemo;
import com.javaguide.database.redis.RedisDemo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * javaguide-database 冒烟测试。
 */
class DatabaseSmokeTest {

    @Test
    void mysqlH2_shouldWork() throws Exception {
        MySqlDemo.main(new String[0]); // 能跑通说明 H2 JDBC + 事务/MVCC 示例正常
        assertTrue(true);
    }

    @Test
    void redisCacheAside_shouldWork() {
        RedisDemo cache = new RedisDemo();
        assertNotNull(cache);
        RedisDemo.main(new String[0]);
    }

    @Test
    void esDsl_shouldBuild() {
        String q = EsDemo.buildBoolFilter("java", "cs", 100);
        assertTrue(q.contains("bool"));
        assertTrue(q.contains("filter"));
        assertTrue(q.contains("range"));
    }
}
