package org.gateway.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
public class FilterUtility {

    private final ObjectMapper mapper = new ObjectMapper();

    public static final FilterUtility INSTANCE = new FilterUtility();

    @SneakyThrows
    public Mono<DataBuffer> getDataBuffer(ServerHttpResponse response, Object httpResponse){
        return Mono.just(
                response.bufferFactory()
                        .wrap(
                                this.mapper
                                        .writeValueAsBytes(httpResponse)
                        )
        );
    }

    public Runnable logResponse(ServerWebExchange exchange){
        return () -> {
            var postResponse = exchange.getResponse();
            var directedRoute = exchange.<URI>getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            log.info("get response: status-code-> {} from URI -> {}", postResponse.getStatusCode(), directedRoute);
        };
    }
}
