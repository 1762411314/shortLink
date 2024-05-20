package org.sulong.project12306.services.userservice.service.handler.filter.user;

import org.sulong.project12306.framework.designpattern.chain.AbstractChainHandler;
import org.sulong.project12306.services.userservice.common.enums.UserChainMarkEnum;
import org.sulong.project12306.services.userservice.dto.req.UserRegisterReqDTO;

public interface UserRegisterCreateChainFilter <T extends UserRegisterReqDTO> extends AbstractChainHandler<T> {
    @Override
    default String mark() {
        return UserChainMarkEnum.USER_REGISTER_FILTER.name();
    }
}
