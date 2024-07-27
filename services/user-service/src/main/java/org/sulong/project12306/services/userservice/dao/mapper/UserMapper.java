package org.sulong.project12306.services.userservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.sulong.project12306.services.userservice.dao.entity.UserDO;

public interface UserMapper extends BaseMapper<UserDO> {
    void deletionUser(UserDO userDO);
}
