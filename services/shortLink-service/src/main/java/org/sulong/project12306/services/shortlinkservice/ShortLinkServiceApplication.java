package org.sulong.project12306.services.shortlinkservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("org.sulong.project12306.services.shortlinkservice.dao.mapper")
@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ShortLinkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortLinkServiceApplication.class, args);
    }

}
