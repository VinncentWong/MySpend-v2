package org.gateway.filter;

import centwong.utility.constant.HttpHeaderConstant;
import centwong.utility.jwt.JwtUtil;
import centwong.utility.response.HttpResponse;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.gateway.utility.FilterUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
@Order(1)
public class JwtFilter implements GlobalFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final FilterUtility filterUtility = FilterUtility.INSTANCE;

    private List<String> nonSecuredEndpoints = List.of(
        "/user/register",
        "/user/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("enter jwt filter");
        var req = exchange
                .getRequest();

        var res = exchange
                .getResponse();

        res.getHeaders()
                .put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_JSON_VALUE));

        Predicate<ServerHttpRequest> isNonSecuredEndpoint = (r) -> {
            var path = r.getURI().getPath();
            log.info("req path: {}", path);
            return nonSecuredEndpoints
                    .stream()
                    .anyMatch((p) -> {

                        var pathContainer = PathContainer.parsePath(path);
                        var pathPattern = PathPatternParser
                                .defaultInstance
                                .parse(p);

                        return pathPattern
                                .matches(pathContainer);
                    });
        };

        if(isNonSecuredEndpoint.test(req)){
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(filterUtility.logResponse(exchange)));
        } else {
            try{
                var headers = req.getHeaders()
                        .get(HttpHeaderConstant.AUTHORIZATION);
                if(headers != null){
                    var header = headers
                            .get(0);
                    if(StringUtils.isBlank(header) || !header.startsWith("Bearer")){
                        var httpResponse = HttpResponse
                                .sendErrorResponse(
                                        "header 'Authorization' value is invalid",
                                        false
                                );
                        return res
                                .writeWith(filterUtility.getDataBuffer(res, httpResponse))
                                .then(Mono.fromRunnable(filterUtility.logResponse(exchange)));
                    } else {
                        var token = header.substring(7);
                        var data = JwtUtil.getTokenData(this.jwtSecret, token);
                        log.info("extracted jwt token: {}", data);
                        exchange = exchange
                                .mutate()
                                .request(
                                        req
                                                .mutate()
                                                .header(HttpHeaderConstant.USER_ID, data.getId() + "")
                                                .build()
                                )
                                .build();
                        return chain
                                .filter(exchange)
                                .then(Mono.fromRunnable(filterUtility.logResponse(exchange)));
                    }
                } else {
                    var httpResponse = HttpResponse
                            .sendErrorResponse(
                                    "header 'Authorization' is required!",
                                    false
                            );
                    return res
                            .writeWith(filterUtility.getDataBuffer(res, httpResponse))
                            .then(Mono.fromRunnable(filterUtility.logResponse(exchange)));
                }
            } catch(Exception ex){
                var httpResponse = HttpResponse
                        .sendErrorResponse(
                                String.format("exception occurred with message: %s", ex.getMessage()),
                                false
                        );
                return res
                        .writeWith(filterUtility.getDataBuffer(res, httpResponse))
                        .then(Mono.fromRunnable(filterUtility.logResponse(exchange)));
            }
        }
    }
}
