package org.sulong.project12306.framework.bases.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sulong.project12306.framework.bases.ApplicationContextHolder;
import org.sulong.project12306.framework.bases.init.ApplicationContentPostProcessor;

public class ApplicationBaseAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder ApplicationContextHolder(){
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContentPostProcessor ApplicationContentPostProcessor(ApplicationContext applicationContext){
        return new ApplicationContentPostProcessor(applicationContext);
    }

}
