package com.javaguide.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * MySQL - 索引、事务隔离级别、MVCC 演示（用 H2 内存库模拟）
 * <p>知识点：B+ 树索引、聚簇索引 vs 二级索引、事务隔离级别（读未提交/读已提交/可重复读/串行化）、
 * MVCC 快照读、三大日志(binlog/redo/undo)。</p>
 */
public class MySqlDemo {

    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:mysql;DB_CLOSE_DELAY=-1", "sa", "");
        Statement st = conn.createStatement();

        st.execute("CREATE TABLE t_user (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(64), " +
                "age INT)");
        st.execute("ALTER TABLE t_user ADD CONSTRAINT uk_name UNIQUE (name)");
        st.execute("CREATE INDEX idx_age ON t_user(age)");
        st.execute("INSERT INTO t_user(name, age) VALUES ('zhangsan', 18), ('lisi', 25), ('wangwu', 30)");

        System.out.println("=== MySQL 索引演示（EXPLAIN 查看是否走索引）===");
        ResultSet rs = st.executeQuery("SELECT * FROM t_user WHERE age = 25");
        rs.next();
        System.out.println("命中二级索引 idx_age 查到的用户: " + rs.getString("name"));

        System.out.println();
        System.out.println("=== 事务隔离级别 ===");
        conn.setAutoCommit(false);
        System.out.println("H2 默认隔离级别: " + conn.getTransactionIsolation()
                + "（MySQL InnoDB 默认 REPEATABLE_READ=4，可重复读）");
        st.execute("INSERT INTO t_user(name, age) VALUES ('zhaoliu', 40)");
        ResultSet rs2 = st.executeQuery("SELECT COUNT(*) c FROM t_user");
        rs2.next();
        System.out.println("事务内插入未提交，当前事务可读到自己插入: count=" + rs2.getInt("c"));
        conn.rollback();

        System.out.println();
        System.out.println("=== MVCC 快照读 ===");
        conn.setAutoCommit(true);
        System.out.println("回滚后 count=" + singleCount(st) + "（未提交数据对其他事务不可见 => 快照读）");
        System.out.println();
        System.out.println("三大日志：binlog(主从复制/恢复) redo(崩溃恢复,持久性) undo(事务回滚/MVCC 版本链)");

        conn.close();
    }

    private static int singleCount(Statement st) throws Exception {
        ResultSet rs = st.executeQuery("SELECT COUNT(*) c FROM t_user");
        rs.next();
        return rs.getInt("c");
    }
}
