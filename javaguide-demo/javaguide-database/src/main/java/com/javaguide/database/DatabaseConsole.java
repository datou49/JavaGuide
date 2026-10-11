package com.javaguide.database;

import com.javaguide.database.es.EsDemo;
import com.javaguide.database.mysql.MySqlDemo;
import com.javaguide.database.redis.RedisDemo;

/**
 * javaguide-database 统一入口：依次运行 MySQL(H2)/Redis/ES 演示。
 */
public class DatabaseConsole {

    public static void main(String[] args) throws Exception {
        System.out.println("========== MySQL（H2 模拟）==========");
        MySqlDemo.main(args);
        System.out.println();

        System.out.println("========== Redis（内存模拟）==========");
        RedisDemo.main(args);
        System.out.println();

        System.out.println("========== Elasticsearch（DSL 模拟）==========");
        EsDemo.main(args);
    }
}
