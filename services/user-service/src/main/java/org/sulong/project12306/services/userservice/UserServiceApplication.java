package org.sulong.project12306.services.userservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.sulong.project12306.services.userservice.dao.entity.UserDO;
import org.sulong.project12306.services.userservice.dao.mapper.UserMapper;

import java.util.Arrays;

@SpringBootApplication
@MapperScan("org.sulong.project12306.services.userservice.dao.mapper")
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {SpringApplication.run(UserServiceApplication.class, args);}
}
