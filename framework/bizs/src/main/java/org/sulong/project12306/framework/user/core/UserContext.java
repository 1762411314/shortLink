package org.sulong.project12306.framework.user.core;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

public class UserContext {
    private final static ThreadLocal<UserInfoDTO> USER_THREAD_LOCAL=new TransmittableThreadLocal<>();

    public static void setUser(UserInfoDTO user){
        USER_THREAD_LOCAL.set(user);
    }

    public static String getUserId(){
        UserInfoDTO userInfoDTO=USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserId).orElse(null);
    }

    public static String getUserName(){
        UserInfoDTO userInfoDTO=USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUsername).orElse(null);
    }

    public static String getRealName() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getRealName).orElse(null);
    }

    public static String getToken() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getToken).orElse(null);
    }

    public static void removeUser(){   USER_THREAD_LOCAL.remove();  }
}
