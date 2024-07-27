package org.sulong.project12306.services.userservice.dto.req;

import lombok.Data;

@Data
public class UserRegisterReqDTO {

    private String username;

    private String password;

    private String realName;

    private Integer idType;

    private String idCard;

    private String phone;

    private String mail;

    private Integer userType;

    private Integer verifyState;

    private String postCode;

    private String address;

    private String region;

    private String telephone;
}
