package org.sulong.project12306.services.gatewayservice.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.sulong.project12306.framework.bases.constant.UserConstant;
import org.sulong.project12306.services.gatewayservice.config.Config;
import org.sulong.project12306.services.gatewayservice.dto.GatewayErrorResult;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Component
public class TokenValidateGatewayFilterFactory extends AbstractGatewayFilterFactory<Config> {

    private final StringRedisTemplate stringRedisTemplate;

    public TokenValidateGatewayFilterFactory(StringRedisTemplate stringRedisTemplate) {
        super(Config.class);
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String requestPath = request.getPath().toString();
            String requestMethod = request.getMethod().name();
            if (!isPathInWhiteList(requestPath, requestMethod, config.getWhitePathList())) {
                String username = request.getHeaders().getFirst(UserConstant.USER_NAME_KEY);
                String token = request.getHeaders().getFirst(UserConstant.USER_TOKEN_KEY);
                if (StringUtils.hasText(username) && StringUtils.hasText(token)) {
                    Object userInfo = stringRedisTemplate.opsForValue().get(token);
                    if (userInfo!=null){
                        JSONObject userInfoJsonObject = JSON.parseObject(userInfo.toString());
                        ServerHttpRequest.Builder builder = exchange.getRequest().mutate().headers(httpHeaders -> {
                            httpHeaders.set(UserConstant.USER_ID_KEY, userInfoJsonObject.getString("userId"));
                            httpHeaders.set(UserConstant.REAL_NAME_KEY, URLEncoder.encode(userInfoJsonObject.getString("realName"), StandardCharsets.UTF_8));
                        });
                        return chain.filter(exchange.mutate().request(builder.build()).build());
                    }
                }
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    GatewayErrorResult resultMessage = GatewayErrorResult.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("Token validation error")
                            .build();
                    return bufferFactory.wrap(JSON.toJSONString(resultMessage).getBytes());
                }));
            }
            return chain.filter(exchange);
        };
    }

    private boolean isPathInWhiteList(String requestPath, String requestMethod, List<String> whitePathList) {
        return (!CollectionUtils.isEmpty(whitePathList) && whitePathList.stream().anyMatch(requestPath::startsWith)) || (Objects.equals(requestPath, "/api/short-link/user/v1") && Objects.equals(requestMethod, "POST"));
    }
}
