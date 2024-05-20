package org.sulong.project12306.services.userservice.dao.mapper;

import org.sulong.project12306.services.userservice.dao.entity.UserMailDO;

public interface UserMailMapper {
    /**
     * 注销用户
     *
     * @param userMailDO 注销用户入参
     */
    void deletionUser(UserMailDO userMailDO);

    void delete(String mail);

    void insert(UserMailDO userMailDO);

    UserMailDO queryUserByMail(String usernameOrMailOrPhone);
}
