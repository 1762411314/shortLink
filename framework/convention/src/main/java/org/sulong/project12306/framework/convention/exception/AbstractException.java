package org.sulong.project12306.framework.convention.exception;

import lombok.Getter;
import org.springframework.util.StringUtils;
import org.sulong.project12306.framework.convention.errorCode.IErrorCode;

import java.util.Optional;

@Getter
public class AbstractException extends RuntimeException{
    public final String errorCode;

    public final String errorMessage;
    public AbstractException(IErrorCode errorCode, String message, Throwable throwable) {
        super(message,throwable);
        this.errorCode=errorCode.code();
        this.errorMessage= Optional.ofNullable(StringUtils.hasLength(message)?message:null).orElse(errorCode.message());

    }
}
