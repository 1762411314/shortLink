package org.sulong.project12306.framework.user.core;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.sulong.project12306.framework.bases.constant.UserConstant;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UserTransmitFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
        String userId=httpServletRequest.getHeader(UserConstant.USER_ID_KEY);
        if (StringUtils.hasText(userId)){
            String userName=httpServletRequest.getHeader(UserConstant.USER_NAME_KEY);
            String realName=httpServletRequest.getHeader(UserConstant.REAL_NAME_KEY);
            if (StringUtils.hasText(userName)){
                userName= URLDecoder.decode(userName, StandardCharsets.UTF_8);
            }
            if (StringUtils.hasText(realName)){
                realName=URLDecoder.decode(realName, StandardCharsets.UTF_8);
            }
            String token=httpServletRequest.getHeader(UserConstant.USER_TOKEN_KEY);
            UserInfoDTO userInfoDTO=UserInfoDTO.builder()
                    .userId(userId)
                    .username(userName)
                    .realName(realName)
                    .token(token)
                    .build();
            UserContext.setUser(userInfoDTO);
        }
        try {
            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            UserContext.removeUser();
        }
    }
}
