package org.sulong.project12306.services.userservice.dao.mapper;

import org.sulong.project12306.services.userservice.dao.entity.UserPhoneDO;

public interface UserPhoneMapper {
    UserPhoneDO queryUserByPhone(String usernameOrMailOrPhone);

    void insert(UserPhoneDO userPhoneDO);

    void deletionUser(UserPhoneDO userPhoneDO);
}
