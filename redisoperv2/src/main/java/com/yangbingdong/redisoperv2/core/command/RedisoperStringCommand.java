package com.yangbingdong.redisoperv2.core.command;

import io.lettuce.core.KeyValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
interface RedisoperStringCommand {
    byte[] get(String key);

    Boolean setEx(String key, long expire, byte[] value);

    CompletableFuture<Boolean> setExAsync(String key, long expire, byte[] value);

    boolean setNx(String key, long expire, byte[] value);

    List<KeyValue<String, byte[]>> mGet(List<String> keys);

    Boolean mSetEx(Map<String, byte[]> map, long expire);

    CompletableFuture<Boolean> mSetExAsync(Map<String, byte[]> map, long expire);

    Long incrBy(String key, long increment);

    Long incrByUsingLua(String key, long increment, long expireSecond, String initValue);

    Long decrBy(String key, long decrement);

    Long decrByUsingLua(String key, long decrement, long min, long expireSecond, String initValue);
}
