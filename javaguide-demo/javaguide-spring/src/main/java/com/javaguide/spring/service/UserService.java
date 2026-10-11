package com.javaguide.spring.service;

import com.javaguide.spring.entity.User;
import com.javaguide.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;

/**
 * Spring - IoC 依赖注入 + 事务示例
 * <p>@Service 注册 Bean；@Autowired 按类型注入；@Qualifier 按名称注入。
 * @Transactional 由 Spring 通过代理 + 事务管理器实现声明式事务（默认传播 REQUIRED、隔离 DEFAULT）。</p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    @Qualifier("asyncPool")
    private ExecutorService asyncPool;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; // 构造器注入
    }

    @Transactional
    public User createUser(User user) {
        User saved = userRepository.save(user);
        if (saved.getAge() > 120) {
            throw new IllegalStateException("年龄过大，触发事务回滚");
        }
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void transferDemo(boolean fail) {
        // 事务回滚演示：两条写操作，任一条异常整体回滚
        User u = new User();
        u.setName("tx-" + System.nanoTime());
        u.setAge(20);
        userRepository.save(u);
        if (fail) {
            throw new IllegalStateException("模拟异常，事务回滚");
        }
    }

    public long count() {
        return userRepository.count();
    }
}
