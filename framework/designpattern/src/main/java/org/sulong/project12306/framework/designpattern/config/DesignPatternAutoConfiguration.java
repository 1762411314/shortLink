package org.sulong.project12306.framework.designpattern.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sulong.project12306.framework.bases.config.ApplicationBaseAutoConfiguration;
import org.sulong.project12306.framework.designpattern.chain.AbstractChainContext;

@ImportAutoConfiguration(ApplicationBaseAutoConfiguration.class)
public class DesignPatternAutoConfiguration {
    @Bean
    public AbstractChainContext abstractChainContext(){
        return new AbstractChainContext();
    }
}
