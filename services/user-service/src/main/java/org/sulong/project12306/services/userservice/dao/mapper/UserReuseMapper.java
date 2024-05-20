package org.sulong.project12306.services.userservice.dao.mapper;

import org.sulong.project12306.services.userservice.dao.entity.UserReuseDO;

public interface UserReuseMapper {
    void delete(String userName);

    void insert(UserReuseDO userReuseDO);
}
