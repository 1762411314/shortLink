package org.sulong.project12306.services.userservice.dto.req;

import lombok.Data;

@Data
public class UserUpdateReqDTO {
    private String id;

    private String username;

    private String mail;

    private Integer userType;

    private String postCode;

    private String address;
}
