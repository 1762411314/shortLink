package org.sulong.project12306.services.userservice.service.handler.filter.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.sulong.project12306.framework.convention.exception.ClientException;
import org.sulong.project12306.services.userservice.common.enums.UserRegisterErrorCodeEnum;
import org.sulong.project12306.services.userservice.dto.req.UserRegisterReqDTO;
import org.sulong.project12306.services.userservice.service.UserLoginService;

@Component
@RequiredArgsConstructor
public final class UserRegisterHasUsernameChainHandler implements UserRegisterCreateChainFilter<UserRegisterReqDTO>{

    private final UserLoginService userLoginService;
    @Override
    public void handler(UserRegisterReqDTO requestParam) {
        if (userLoginService.hasUsername(requestParam.getUsername())){
            throw new ClientException(UserRegisterErrorCodeEnum.HAS_USERNAME_NOTNULL);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
