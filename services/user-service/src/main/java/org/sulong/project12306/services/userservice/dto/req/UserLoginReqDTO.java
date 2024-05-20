package org.sulong.project12306.services.userservice.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginReqDTO {

    private String usernameOrMailOrPhone;

    private String password;
}
