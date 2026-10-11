package com.javaguide.distributed.gateway;

/**
 * API 网关(Spring Cloud Gateway) 概念演示
 * <p>网关职责：路由转发、负载均衡、统一鉴权、限流、跨域、日志。
 * 工作原理：客户端请求 -> Route(路由) 匹配 -> Predicate(断言) -> Filter(过滤器) 链 -> 转发到下游服务。
 * gulimall 网关 88 端口，按路径 /api/product/** 路由到 product 服务。</p>
 */
public class GatewayDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring Cloud Gateway ===");
        System.out.println("Route:  id + uri + predicates + filters");
        System.out.println("Predicate(断言): Path=/api/product/**, Method=GET 等");
        System.out.println("Filter(过滤器): 全局 GlobalFilter / 局部 GatewayFilter");
        System.out.println("路由示例:  Path=/api/product/**  ->  lb://gulimall-product  (负载均衡)");

        System.out.println();
        System.out.println("典型过滤器：StripPrefix(去前缀)、AddRequestHeader(加头)、RequestRateLimiter(限流)");
        System.out.println("WebFlux 响应式，基于 Netty，非 Servlet");
    }
}
