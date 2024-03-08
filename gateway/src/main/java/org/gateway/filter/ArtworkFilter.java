package org.gateway.filter;

import centwong.utility.constant.HttpHeaderConstant;
import centwong.utility.response.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.user.constant.Constant;
import org.artwork.constant.EndpointConstant;
import org.user.entity.User;
import org.gateway.utility.FilterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

// Only artist allowed to access this datasource
@Component
@Slf4j
public class ArtworkFilter extends AbstractGatewayFilterFactory<ArtworkFilter.Config> {

    @Autowired
    private WebClient.Builder webClient;

    private final FilterUtility filterUtility = FilterUtility.INSTANCE;

    @Autowired
    private ObjectMapper mapper;

    public ArtworkFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            log.info("enter artwork filter");
            var req = exchange.getRequest();
            var res = exchange.getResponse();
            var headers = req.getHeaders()
                    .get(HttpHeaderConstant.USER_ID);

            res.getHeaders()
                    .put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_JSON_VALUE));

            if(headers == null){
                var httpResponse = HttpResponse
                        .sendErrorResponse(
                                "user id required on header 'X-User-Id'",
                                false
                        );
                return res
                        .writeWith(filterUtility.getDataBuffer(res, httpResponse));
            }

            var id = headers.get(0);

            var monoHttpRes = this.webClient
                    .baseUrl(String.format("http://%s/user/%s", EndpointConstant.USER_SERVICE, id))
                    .build()
                    .get()
                    .header(HttpHeaderConstant.USER_ID, String.valueOf(id))
                            .retrieve()
                            .bodyToMono(HttpResponse.class);

            return monoHttpRes.flatMap((httpResp) -> {
                log.info("finished fetching service with data: {}", httpResp);

                var data = httpResp.getData();

                if(data == null){
                    var httpResponse = HttpResponse
                            .sendErrorResponse(
                                    "user doesn't exist",
                                    false
                            );
                    return res
                            .writeWith(filterUtility.getDataBuffer(res, httpResponse));
                }

                var user = mapperReadValue(
                        data, User.class
                );

                log.info("extracted user data: {}", data);

                if(!user.getRole().equals(Constant.ARTIST)){
                    var httpResponse = HttpResponse
                            .sendErrorResponse(
                                    "only artist allowed to post artwork",
                                    false
                            );
                    return res
                            .writeWith(filterUtility.getDataBuffer(res, httpResponse));
                }

                return chain.filter(exchange);
            });
        }, Ordered.LOWEST_PRECEDENCE);
    }

    public static class Config{}

    @SneakyThrows
    public <T> T mapperReadValue(Object data, Class<T> clazz){
        return this.mapper.readValue(mapperWriteToString(data), clazz);
    }

    @SneakyThrows
    public String mapperWriteToString(Object data){
        return this.mapper.writeValueAsString(data);
    }

}
