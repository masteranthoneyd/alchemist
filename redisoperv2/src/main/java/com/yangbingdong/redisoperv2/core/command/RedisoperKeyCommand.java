package com.yangbingdong.redisoperv2.core.command;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
interface RedisoperKeyCommand {
    Long del(String... keys);

    void delByScan(String pattern);

    CompletableFuture<Long> delAsync(String... keys);

    CompletableFuture<Long> unlinkAsync(String... keys);

    boolean exists(String key);

    CompletableFuture<Boolean> expireAsync(String key, long second);

    CompletableFuture<Boolean> batchExpireUsingLua(List<String> keys, long second);

    CompletableFuture<Boolean> expireAtAsync(String key, long timestamp);

    Long ttl(String key);
}
