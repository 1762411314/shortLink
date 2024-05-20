package org.sulong.project12306.framework.bases.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.sulong.project12306.framework.bases.ApplicationContextHolder;
import org.sulong.project12306.framework.bases.init.ApplicationContentPostProcessor;

public class ApplicationBaseAutoConfiguration {
    @Bean
    @ConditionalOnWebApplication
    public ApplicationContextHolder ApplicationContextHolderBean(){
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnWebApplication
    public ApplicationContentPostProcessor ApplicationContentPostProcessorBean(ApplicationContext applicationContext){
        return new ApplicationContentPostProcessor(applicationContext);
    }

}
