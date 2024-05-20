package org.sulong.project12306.services.gatewayservice.config;

import lombok.Data;

import java.util.List;

/**
 * 过滤器配置
 */
@Data
public class Config {

    /**
     * 白名单前置路径
     */
    private List<String> whitePathList=List.of("/api/short-link/user/v1/login","/api/short-link/user/v1/has-username");
}
