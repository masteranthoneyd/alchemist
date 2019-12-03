package com.yangbingdong.redisoperv2.core.wrapper;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
@SuppressWarnings({"unchecked", "unused"})
@Data
@Accessors(chain = true)
public class SerializeWrapper {
    private Object data;
    private long expireTime;


    public static SerializeWrapper of(Object data) {
        return new SerializeWrapper().setData(data);
    }

    public static SerializeWrapper of(Object data, long expireTime) {
        return new SerializeWrapper().setData(data).setExpireTime(expireTime);
    }

    public <T> T getRawData(Class<T> clazz) {
        return (T) data;
    }

    public <T> List<T> getList(Class<T> clazz) {
        return (List<T>) data;
    }

    public <K, V> Map<K, V> getMap(Class<K> kClass, Class<V> vClass) {
        return (Map<K, V>) data;
    }

}

