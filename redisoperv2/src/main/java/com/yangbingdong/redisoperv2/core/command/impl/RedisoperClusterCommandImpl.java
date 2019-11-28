package com.yangbingdong.redisoperv2.core.command.impl;

import com.yangbingdong.redisoperv2.LettuceResourceProvider;
import com.yangbingdong.redisoperv2.core.command.RedisoperCommand;
import io.lettuce.core.KeyValue;
import io.lettuce.core.SetArgs;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
@SuppressWarnings("unchecked")
public class RedisoperClusterCommandImpl implements RedisoperCommand {

    private final RedisAdvancedClusterCommands<String, byte[]> command;
    private final RedisAdvancedClusterAsyncCommands<String, byte[]> asyncCommand;

    public RedisoperClusterCommandImpl(LettuceResourceProvider lettuceResourceProvider) {
        command = lettuceResourceProvider.getClusterCommand();
        asyncCommand = lettuceResourceProvider.getClusterAsyncCommand();
    }

    /*########## Key ##########*/

    @Override
    public Long del(String... keys) {
        return command.del(keys);
    }

    @Override
    public CompletableFuture<Long> delAsync(String... keys) {
        return (CompletableFuture<Long>) asyncCommand.del(keys);
    }

    @Override
    public CompletableFuture<Long> unlinkAsync(String... keys) {
        return (CompletableFuture<Long>) asyncCommand.unlink(keys);
    }

    @Override
    public boolean exists(String key) {
        return LONG_ONE.equals(command.exists(key));
    }

    @Override
    public CompletableFuture<Boolean> expireAsync(String key, long second) {
        return (CompletableFuture<Boolean>) asyncCommand.expire(key, second);
    }

    @Override
    public CompletableFuture<Boolean> expireAtAsync(String key, long timestamp) {
        return (CompletableFuture<Boolean>) asyncCommand.expireat(key, timestamp);
    }

    @Override
    public Long ttl(String key) {
        return command.ttl(key);
    }

    /*########## String ##########*/

    @Override
    public byte[] get(String key) {
        return command.get(key);
    }

    @Override
    public Boolean setEx(String key, long expire, byte[] value) {
        return isOk(command.setex(key, expire, value));
    }

    @Override
    public CompletableFuture<Boolean> setExAsync(String key, long expire, byte[] value) {
        asyncCommand.setex(key, expire, value);
        return null;
    }

    @Override
    public boolean setNx(String key, long expire, byte[] value) {
        return isOk(command.set(key, value, SetArgs.Builder.nx().ex(expire)));
    }

    @Override
    public List<KeyValue<String, byte[]>> mGet(List<String> keys) {
        return command.mget(keys.toArray(STRING_ARRAY_TMP));
    }

    @Override
    public Boolean mSetEx(Map<String, byte[]> map, long expire) {
        String result = command.mset(map);
        map.forEach((k, v) -> asyncCommand.expire(k, expire));
        return isOk(result);
    }

    @Override
    public CompletableFuture<Boolean> mSetExAsync(Map<String, byte[]> map, long expire) {
        CompletableFuture<String> mset = (CompletableFuture<String>) asyncCommand.mset(map);
        return mset.thenApply(s -> {
            map.forEach((k, v) -> asyncCommand.expire(k, expire));
            return isOk(s);
        });
    }
}
