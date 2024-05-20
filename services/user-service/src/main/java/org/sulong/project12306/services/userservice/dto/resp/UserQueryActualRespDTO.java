package org.sulong.project12306.services.userservice.dto.resp;

import lombok.Data;

@Data
public class UserQueryActualRespDTO {

    private String username;

    private String realName;

    private String region;

    private Integer idType;

    private String idCard;

    private String phone;

    private String telephone;

    private String mail;

    private Integer userType;

    private Integer verifyStatus;

    private String postCode;

    private String address;
}
