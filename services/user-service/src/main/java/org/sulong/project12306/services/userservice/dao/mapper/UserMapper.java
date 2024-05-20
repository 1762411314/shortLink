package org.sulong.project12306.services.userservice.dao.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.sulong.project12306.services.userservice.dao.entity.UserDO;
import org.sulong.project12306.services.userservice.dao.entity.UserPhoneDO;

@Mapper
public interface UserMapper {
    void deletionUser(UserDO userDO);

    UserDO queryUserByUserId(String userId);


    UserDO queryUserByUsername(String username);

    UserDO loginByUsername(String username,String password);
    Long queryUserDeletionNum(Integer idType, String idCard);

    void update(UserDO userDO);

    int insert(UserDO convert);

}
