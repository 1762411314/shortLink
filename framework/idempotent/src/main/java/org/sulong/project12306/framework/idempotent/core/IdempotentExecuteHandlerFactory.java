package org.sulong.project12306.framework.idempotent.core;

import org.sulong.project12306.framework.bases.ApplicationContextHolder;
import org.sulong.project12306.framework.idempotent.core.param.IdempotentParamService;
import org.sulong.project12306.framework.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.token.IdempotentTokenService;
import org.sulong.project12306.framework.idempotent.enums.IdempotentSceneEnum;
import org.sulong.project12306.framework.idempotent.enums.IdempotentTypeEnum;

public final class IdempotentExecuteHandlerFactory {
    public static IdempotentExecuteHandler getInstance(IdempotentSceneEnum scene, IdempotentTypeEnum type){
        IdempotentExecuteHandler result=null;
        switch (scene) {
            case RESTAPI -> {
                switch (type) {
                    case PARAM -> result = ApplicationContextHolder.getBean(IdempotentParamService.class);
                    case TOKEN -> result = ApplicationContextHolder.getBean(IdempotentTokenService.class);
                    case SPEL -> result = ApplicationContextHolder.getBean(IdempotentSpELByRestAPIExecuteHandler.class);
                    default -> {
                    }
                }
            }
            case MQ -> result = ApplicationContextHolder.getBean(IdempotentSpELByMQExecuteHandler.class);
            default -> {
            }
        }
        return result;
    }
}
