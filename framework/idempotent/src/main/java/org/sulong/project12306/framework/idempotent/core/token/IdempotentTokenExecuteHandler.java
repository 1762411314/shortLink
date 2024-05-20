package org.sulong.project12306.framework.idempotent.core.token;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.sulong.project12306.framework.cache.DistributedCache;
import org.sulong.project12306.framework.convention.errorCode.BaseErrorCode;
import org.sulong.project12306.framework.convention.exception.ClientException;
import org.sulong.project12306.framework.idempotent.config.IdempotentProperties;
import org.sulong.project12306.framework.idempotent.core.AbstractIdempotentExecuteHandler;
import org.sulong.project12306.framework.idempotent.core.IdempotentParamWrapper;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class IdempotentTokenExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentTokenService {

    private final IdempotentProperties idempotentProperties;

    private final DistributedCache distributedCache;
    private static final String TOKEN_KEY = "token";
    private static final String TOKEN_PREFIX_KEY = "idempotent:token:";
    private static final long TOKEN_EXPIRED_TIME = 6000;
    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        return new IdempotentParamWrapper();
    }

    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        HttpServletRequest request=((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token=request.getHeader(TOKEN_KEY);
        if (StrUtil.isBlank(token)){
            token=request.getParameter(TOKEN_KEY);
            if (StrUtil.isBlank(token)) {
                throw new ClientException(BaseErrorCode.IDEMPOTENT_TOKEN_NULL_ERROR);
            }
        }
        Boolean tokenDelFlag=distributedCache.delete(token);
        if (!tokenDelFlag){
            String errMsg = StrUtil.isNotBlank(wrapper.getIdempotent().message())
                    ? wrapper.getIdempotent().message()
                    : BaseErrorCode.IDEMPOTENT_TOKEN_DELETE_ERROR.message();
            throw new ClientException(errMsg, BaseErrorCode.IDEMPOTENT_TOKEN_DELETE_ERROR);
        }

    }

    @Override
    public String createToken() {
        String token= Optional.ofNullable(StrUtil.emptyToNull(idempotentProperties.getPrefix()))
                .orElse(TOKEN_PREFIX_KEY)+ UUID.randomUUID();
        distributedCache.put(token,"",Optional.ofNullable(idempotentProperties.getTimeout()).orElse(TOKEN_EXPIRED_TIME));
        return token;
    }
}
