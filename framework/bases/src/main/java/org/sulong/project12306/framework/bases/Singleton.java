package org.sulong.project12306.framework.bases;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public final class Singleton {
    private static final ConcurrentMap<String,Object> SINGLE_OBJECT_POOL=new ConcurrentHashMap<>();

    public static <T> T get(String key){
        Object result=SINGLE_OBJECT_POOL.get(key);
        return (result==null)?null:(T) result;
    }

    public static <T> T get(String key, Supplier<T> supplier){
        Object result=SINGLE_OBJECT_POOL.get(key);
        if (result==null&&(result=supplier.get())!=null){
            SINGLE_OBJECT_POOL.put(key,result);
        }
        return (result!=null)?(T) result:null;
    }

    public static void put(Object value){
        put(value.getClass().getName(),value);
    }

    public static void put(String name,Object value){
        SINGLE_OBJECT_POOL.put(name,value);
    }
}
