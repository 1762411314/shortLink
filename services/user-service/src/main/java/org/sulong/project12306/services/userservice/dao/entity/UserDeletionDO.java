package org.sulong.project12306.services.userservice.dao.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDeletionDO {
    /**
     * id
     */
    private Long id;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 证件号
     */
    private String idCard;
}
