package org.sulong.project12306.services.userservice.dto.req;

import lombok.Data;

@Data
public class PassengerReqDTO {
    private String id;

    private String realName;

    private Integer idType;

    private String idCard;

    private Integer discountType;

    private String phone;
}
