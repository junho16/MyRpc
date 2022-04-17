package com.nwpu.util.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例工厂
 * @author Junho
 * @date 2022/4/17 22:26
 */
public class SingleFactory {

    private static Map<Class, Object> objectMap = new HashMap<>();

    private SingleFactory() {}

    public static <T> T getInstance(Class<T> clazz) {
        Object instance = objectMap.get(clazz);
        synchronized (clazz) {
            if(instance == null) {
                try {
                    instance = clazz.newInstance();
                    objectMap.put(clazz, instance);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return clazz.cast(instance);
    }
}
