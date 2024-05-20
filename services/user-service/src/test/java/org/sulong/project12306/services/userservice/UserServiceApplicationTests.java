package org.sulong.project12306.services.userservice;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sulong.project12306.services.userservice.dao.entity.UserDO;
import org.sulong.project12306.services.userservice.dao.mapper.UserMapper;

@SpringBootTest

class UserServiceApplicationTests {
    @Autowired
    private UserMapper userMapper;

}
