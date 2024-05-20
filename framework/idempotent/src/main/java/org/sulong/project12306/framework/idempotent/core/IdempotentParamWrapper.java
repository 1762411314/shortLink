package org.sulong.project12306.framework.idempotent.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.sulong.project12306.framework.idempotent.annotation.Idempotent;
import org.sulong.project12306.framework.idempotent.enums.IdempotentTypeEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public final class IdempotentParamWrapper {
    /**
     * 幂等注解
     */
    private Idempotent idempotent;

    /**
     * 锁标识，{@link IdempotentTypeEnum#PARAM}
     */
    private String lockKey;
    /**
     * AOP 处理连接点
     */
    private ProceedingJoinPoint joinPoint;

}
