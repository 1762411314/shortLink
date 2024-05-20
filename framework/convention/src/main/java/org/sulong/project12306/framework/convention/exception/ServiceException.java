package org.sulong.project12306.framework.convention.exception;

import org.sulong.project12306.framework.convention.errorCode.BaseErrorCode;
import org.sulong.project12306.framework.convention.errorCode.IErrorCode;

import java.util.Optional;

public class ServiceException extends AbstractException {


    public ServiceException(String message) {
        this(message, BaseErrorCode.SERVICE_ERROR, null);
    }

    public ServiceException(IErrorCode errorCode){
        this(null,errorCode);
    }

    public ServiceException(String message,IErrorCode errorCode){
        this(message,errorCode,null);
    }

    public ServiceException(String message, IErrorCode errorCode, Throwable throwable) {
        super(errorCode,Optional.ofNullable(message).orElse(errorCode.message()), throwable );
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
