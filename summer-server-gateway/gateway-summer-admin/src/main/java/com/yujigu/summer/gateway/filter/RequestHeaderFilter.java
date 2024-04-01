package com.yujigu.summer.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: 中实现获取真实客户端IP地址
 * @BelongsProject: ocean
 * @BelongsPackage: com.ibalbal.ocean.config.filter
 * @Author: ly
 * @CreateTime: 2023-05-19  17:26
 * @Version: 1.0
 */
@Slf4j
@Component
public class RequestHeaderFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获得路由
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        // 配置文件中配置的route的uri属性(匹配到的route),本例中是http://127.0.0.1:8001
        String uri = route.getUri().toString();

        ServerHttpRequest request = exchange.getRequest();
        // 请求路径中域名之后的部分,本例中是/api/name/get
        String path = request.getPath().toString();

        // 转发后的完整地址,本例中是http://127.0.0.1:8001/api/name/get
        String address = uri + path;


        log.info("请求地址：{}", address);
        return chain.filter(exchange);

//        ServerHttpRequest request = exchange.getRequest();
//        HttpHeaders headers = request.getHeaders();
//
//        ServerHttpRequest.Builder mutate = request.mutate();
//        List<String> originalForwardedFor = headers.get("X-Forwarded-For");
//        String remoteAddress = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
//
//        mutate.header("X-Real-IP",exchange.getRequest().getRemoteAddress().getHostString());
//        if (originalForwardedFor == null) {
//            mutate.header("X-Forwarded-For", remoteAddress);
//        } else {
//            String newForwardedFor = originalForwardedFor.get(0) + ", " + remoteAddress;
//            mutate.header("X-Forwarded-For", newForwardedFor);
//        }

//        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

}
