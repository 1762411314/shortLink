package org.sulong.project12306.framework.idempotent.core.token;

import org.sulong.project12306.framework.idempotent.core.IdempotentExecuteHandler;

/**
 * Token 实现幂等接口
 */
public interface IdempotentTokenService extends IdempotentExecuteHandler {

    /**
     * 创建幂等验证Token
     */
    String createToken();
}
