package org.sulong.project12306.framework.user.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.sulong.project12306.framework.bases.constant.FilterOrderConstant;
import org.sulong.project12306.framework.user.core.UserContext;
import org.sulong.project12306.framework.user.core.UserInfoDTO;
import org.sulong.project12306.framework.user.core.UserTransmitFilter;

@ConditionalOnWebApplication
public class UserAutoConfiguration {
    @Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter(){
        FilterRegistrationBean<UserTransmitFilter> registration=new FilterRegistrationBean<>();
        registration.setFilter(new UserTransmitFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(FilterOrderConstant.USER_TRANSMIT_FILTER_ORDER);
        return registration;
    }
}
