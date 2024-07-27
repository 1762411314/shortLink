package org.sulong.project12306.services.userservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.sulong.project12306.services.userservice.dao.entity.UserReuseDO;

public interface UserReuseMapper extends BaseMapper<UserReuseDO> {
    void delete(String userName);
}
