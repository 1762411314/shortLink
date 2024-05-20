package org.sulong.project12306.services.userservice.dao.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPhoneDO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 注销时间戳
     */
    private Long deletionTime;
}
