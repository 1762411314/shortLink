package org.sulong.project12306.framework.idempotent.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum IdempotentMQConsumeStatusEnum {
    CONSUMING("1"),

    CONSUMED("0");

    private final String code;

    public boolean isError(String consumeStatus){
        return Objects.equals(consumeStatus,CONSUMING.code);
    }
}
