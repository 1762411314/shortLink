package org.sulong.project12306.framework.idempotent.core;

import cn.hutool.core.collection.CollUtil;

import java.util.HashMap;
import java.util.Map;

public final class IdempotentContext {
    private static final ThreadLocal<Map<String,Object>> CONTEXT=new ThreadLocal<>();

    private static Map<String,Object> get(){
        return CONTEXT.get();
    }

    public static Object getKey(String key){
        Map<String,Object> context=get();
        if (CollUtil.isNotEmpty(context)){
            return context.get(key);
        }
        return null;
    }

    public static String getString(String key){
        Object result=getKey(key);
        if (result!=null)
            return result.toString();
        return null;
    }

    public static void put(String key,Object object){
        Map<String,Object> context=get();
        if (CollUtil.isEmpty(context)){
            context=new HashMap<>();
        }
        context.put(key,object);
    }

    public static void putContext(Map<String, Object> context) {
        Map<String, Object> threadContext = CONTEXT.get();
        if (CollUtil.isNotEmpty(threadContext)) {
            threadContext.putAll(context);
            return;
        }
        CONTEXT.set(context);
    }

    public static void clean() {
        CONTEXT.remove();
    }
}
