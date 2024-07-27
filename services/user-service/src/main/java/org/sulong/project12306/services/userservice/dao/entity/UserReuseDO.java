package org.sulong.project12306.services.userservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("t_user_reuse")
public class UserReuseDO {
    /**
     * 用户名
     */
    private String username;

}
