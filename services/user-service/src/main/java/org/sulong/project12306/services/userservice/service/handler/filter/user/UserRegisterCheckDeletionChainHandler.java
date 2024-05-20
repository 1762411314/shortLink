package org.sulong.project12306.services.userservice.service.handler.filter.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.sulong.project12306.framework.convention.exception.ClientException;
import org.sulong.project12306.services.userservice.dto.req.UserRegisterReqDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserRegisterRespDTO;
import org.sulong.project12306.services.userservice.service.UserService;

@Component
@RequiredArgsConstructor
public final class UserRegisterCheckDeletionChainHandler implements UserRegisterCreateChainFilter<UserRegisterReqDTO> {
    private final UserService userService;

    @Override
    public void handler(UserRegisterReqDTO requestParam) {
        Integer userDeletionNum=userService.queryUserDeletionNum(requestParam.getIdType(),requestParam.getIdCard());
        if (userDeletionNum>5){
            throw new ClientException("证件号多次注销账号已被加入黑名单");
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }

}
