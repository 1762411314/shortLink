package org.sulong.project12306.framework.user.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserInfoDTO {
    private String userId;

    private String username;

    private String realName;

    private String token;
}
