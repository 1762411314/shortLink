package org.sulong.project12306.services.userservice.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.sulong.project12306.services.userservice.dao.entity.UserDeletionDO;

public interface UserDeletionMapper extends BaseMapper<UserDeletionDO> {
    Long selectCount(LambdaQueryWrapper<UserDeletionDO> queryWrapper);
}
