# JavaGuide 知识点覆盖 Demo

一个 **Maven 多模块** 的 Java 后端知识点示例工程，目标是用小而精的代码示例覆盖
[JavaGuide](https://github.com/Snailclimb/JavaGuide) 的核心高频知识点，并**尽量自包含、开箱可跑**。

- **JDK 21** + **Spring Boot 3.5.6** + **Spring Cloud 2024.0.2**
- 数据库用 **H2 内存库**；Redis / MQ / 注册中心 / 网关等以「内存模拟 + 概念演示」为主
- 每个知识点带可运行 `main` 入口 + JUnit 冒烟测试

## 快速开始

```bash
# 在 javaguide-demo 目录下
mvn test                 # 运行全部模块测试（19 个用例，无需任何外部中间件）
mvn package -DskipTests  # 打包（javaguide-spring 产出可执行 jar）
```

### 各模块可运行入口

| 模块 | 入口 | 运行方式 |
| --- | --- | --- |
| javaguide-core | `com.javaguide.core.ConsoleDemo` | `mvn -q -pl javaguide-core compile exec:java "-Dexec.mainClass=com.javaguide.core.ConsoleDemo"` |
| javaguide-database | `com.javaguide.database.DatabaseConsole` | 同上，换 mainClass |
| javaguide-mq | `com.javaguide.mq.MqConsole` | 同上 |
| javaguide-distributed | `com.javaguide.distributed.DistributedConsole` | 同上 |
| javaguide-highconcurrency | `com.javaguide.highconcurrency.HighConcurrencyConsole` | 同上 |
| javaguide-spring | `SpringDemoApplication`（web 应用） | `java -jar javaguide-spring/target/javaguide-spring-*.jar`，端口 18080 |

> Windows 控制台中文乱码只是代码页问题，不影响逻辑与结果。

### javaguide-spring REST 接口

| 接口 | 说明 |
| --- | --- |
| `GET /api/hello` | 基础接口 + 配置读取 |
| `GET /api/ioc/beans` | IoC 容器 Bean、代理类名（AOP 生效证明） |
| `POST /api/user` | JSR-303 参数校验（非法输入返回 400） |
| `POST /api/order/pay` body `{"fail":true}` | `@Transactional` 回滚演示 |
| `GET /api/auto-config` | 自动装配说明 |

## 模块结构与知识点对照（JavaGuide 知识地图）

### 1. javaguide-core — Java 基础 / 集合 / 并发 / JVM

| JavaGuide 知识点 | 示例类 |
| --- | --- |
| 为什么 Java 只有值传递 | `basis/ValuePassingDemo` |
| 反射机制 | `basis/ReflectionDemo` |
| 代理模式（JDK 动态代理） | `basis/ProxyDemo` |
| HashMap 核心原理 / 扩容阈值 | `collection/HashMapDemo` |
| ConcurrentHashMap 线程安全 | `collection/ConcurrentHashMapDemo` |
| 线程池详解（核心参数/拒绝策略） | `concurrent/ThreadPoolDemo` |
| Java 锁（ReentrantLock） | `concurrent/ReentrantLockDemo` |
| CAS 与原子类 / ABA | `concurrent/CasDemo` |
| AQS（CountDownLatch / Semaphore） | `concurrent/AqsDemo` |
| ThreadLocal 线程隔离 | `concurrent/ThreadLocalDemo` |
| CompletableFuture 异步编排 | `concurrent/CompletableFutureDemo` |
| 虚拟线程（JDK21 新特性） | `concurrent/VirtualThreadDemo` |
| JVM 内存区域 | `jvm/MemoryAreaDemo` |
| JVM 垃圾回收 / 堆 | `jvm/GcDemo` |
| 类加载过程 / 类加载器 | `jvm/ClassLoadingDemo` |

### 2. javaguide-database — MySQL / Redis / ES

| JavaGuide 知识点 | 示例类 |
| --- | --- |
| MySQL 索引 / 事务隔离级别 / MVCC / 三大日志 | `mysql/MySqlDemo`（H2 模拟） |
| Redis 5 种数据结构 / 缓存读写策略 / 缓存三兄弟 / LRU | `redis/RedisDemo` |
| ES 检索 DSL（match / phrase / bool / term / 聚合） | `es/EsDemo` |

### 3. javaguide-spring — Spring / SpringBoot

| JavaGuide 知识点 | 示例 |
| --- | --- |
| IoC 容器 / 依赖注入 / @Bean | `config/AppConfig`、`service/UserService` |
| AOP（@Before/@After/@Around，代理） | `aop/LogAspect` |
| 声明式事务 @Transactional（回滚） | `service/UserService.transferDemo` |
| SpringBoot 自动装配 | `DemoController.autoConfig` |
| RESTful / JSR-303 参数校验 / 全局异常 | `web/DemoController`、`web/GlobalExceptionHandler` |

### 4. javaguide-mq — 消息队列 RabbitMQ

| JavaGuide 知识点 | 示例类 |
| --- | --- |
| Exchange 类型（Direct / Fanout / Topic） | `ExchangeDemo` |
| 延时队列（TTL + 死信）模拟订单关单 | `DelayQueueDemo` |
| 可靠投递 / 消息丢失、重复、积压 | `ReliableDeliveryDemo` |

### 5. javaguide-distributed — 分布式体系

| JavaGuide 知识点 | 示例类 |
| --- | --- |
| CAP / BASE / Raft / 一致性哈希 | `CapDemo` |
| 分布式锁（Redis SETNX / Redisson） | `lock/DistributedLockDemo` |
| 分布式 ID（雪花算法） | `id/DistributedIdDemo` |
| 分布式事务（2PC / TCC / 本地消息表 / Seata） | `tx/DistributedTxDemo` |
| 注册中心 / 配置中心（Nacos） | `config/NacosDemo` |
| API 网关（Spring Cloud Gateway） | `gateway/GatewayDemo` |

### 6. javaguide-highconcurrency — 高可用 / 高性能

| JavaGuide 知识点 | 示例类 |
| --- | --- |
| 限流算法（令牌桶等） | `ratelimit/RateLimitDemo` |
| 熔断 / 降级 / 超时重试 | `resilience/CircuitBreakerDemo` |
| 缓存穿透 / 击穿 / 雪崩（互斥锁） | `cache/CacheProtectionDemo` |
| 布隆过滤器 | `cache/BloomFilterDemo` |
| 线程池隔离（舱壁模式） | `pool/ThreadPoolIsolationDemo` |
| 负载均衡算法 | `loadbalance/LoadBalanceDemo` |
| 读写分离 / 分库分表 / SQL 优化 | `db/DbShardingDemo` |

## 与谷粒商城（gulimall）学习清单的关系

本 demo 是对 `gulimall` 系列课程的「知识点验证版」：课程里那些需要整套
MySQL/Redis/ES/RabbitMQ/Nacos 环境的中间件知识（分布式锁、缓存三兄弟、延时队列关单、ES 检索 DSL、
Nacos 注册/配置中心、网关路由、Sentinel 流控），在这里都能用最小化代码看到**核心语义与正确性验证**。

## 目录结构

```
javaguide-demo/
├── pom.xml                     # 父 POM（Spring Boot 3.5.6）
├── javaguide-core/             # Java 基础/集合/并发/JVM
├── javaguide-database/         # MySQL(H2)/Redis/ES
├── javaguide-spring/           # SpringBoot Web 应用（可启动，端口 18080）
├── javaguide-mq/               # RabbitMQ 概念
├── javaguide-distributed/      # 分布式体系
└── javaguide-highconcurrency/  # 高可用/高性能
```
