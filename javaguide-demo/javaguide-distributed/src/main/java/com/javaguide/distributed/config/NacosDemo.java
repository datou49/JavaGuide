package com.javaguide.distributed.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心(Nacos) / 配置中心 概念演示
 * <p>Nacos 两大能力：服务注册与发现（注册中心）+ 动态配置（配置中心）。
 * 服务实例注册 -> 心跳续约 -> 服务消费者通过 Nacos 获取可用实例列表（负载均衡/故障剔除）。</p>
 */
public class NacosDemo {

    static class Registry {
        final Map<String, String> services = new ConcurrentHashMap<>();

        void register(String service, String instance) {
            services.put(service, instance);
            System.out.println("Nacos 注册中心: " + service + " 注册实例 " + instance);
        }

        void heartbeat(String service, String instance) {
            System.out.println("心跳续约: " + service + "/" + instance + "，每 5s 一次，15s 未续约则剔除");
        }

        String discover(String service) {
            return services.get(service) + "（服务发现 -> 负载均衡调用）";
        }
    }

    static class ConfigCenter {
        final Map<String, String> configs = new ConcurrentHashMap<>();

        void publish(String key, String value) {
            configs.put(key, value);
            System.out.println("配置中心 Nacos Config 发布: " + key + " = " + value);
            System.out.println("服务端收到配置变更 -> 动态刷新（@RefreshScope）");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 注册中心（Nacos Discovery）===");
        Registry registry = new Registry();
        registry.register("gulimall-product", "192.168.0.246:10000");
        registry.register("gulimall-order", "192.168.0.246:9000");
        registry.heartbeat("gulimall-product", "192.168.0.246:10000");
        System.out.println("Feign 调用: " + registry.discover("gulimall-product"));

        System.out.println();
        System.out.println("=== 配置中心（Nacos Config）===");
        ConfigCenter cc = new ConfigCenter();
        cc.publish("common.mysql.url", "jdbc:mysql://192.168.0.246:3306/gm_pms");
        cc.publish("common.redis.host", "192.168.0.246:6379");
        System.out.println("多服务共享配置 + 动态刷新，无需重启");
    }
}
