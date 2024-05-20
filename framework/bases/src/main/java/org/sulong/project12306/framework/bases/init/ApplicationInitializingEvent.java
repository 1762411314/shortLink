package org.sulong.project12306.framework.bases.init;

import org.springframework.context.ApplicationEvent;

public class ApplicationInitializingEvent extends ApplicationEvent {
    public ApplicationInitializingEvent(Object source){
        super(source);
    }
}
