package com.youngbingdong.util.perf.spel;

import java.lang.reflect.Method;

/**
 * @author yangbingdong
 */
public class SpelPayload {

    public static Object target = new SpelPayload();
    public static Class<?> targetClass = target.getClass();
    public static Method method;
    public static Object[] args = new Object[]{"yangbingdong"};
    public static String spel = "'CacheName_'+#name";


    static {
        try {
            method = SpelPayload.class.getDeclaredMethod("spel", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String spel(String name) {
        return "Hello " + name;
    }
}
