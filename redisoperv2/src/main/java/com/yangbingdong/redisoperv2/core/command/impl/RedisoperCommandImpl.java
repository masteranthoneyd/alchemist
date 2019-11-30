package com.yangbingdong.redisoperv2.core.command.impl;

import com.yangbingdong.redisoperv2.LettuceResourceProvider;
import com.yangbingdong.redisoperv2.core.command.RedisoperCommand;
import io.lettuce.core.KeyValue;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.protocol.AsyncCommand;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.valueOf;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
@SuppressWarnings("unchecked")
public class RedisoperCommandImpl implements RedisoperCommand {

    private final RedisCommands<String, byte[]> command;
    private final RedisAsyncCommands<String, byte[]> asyncCommand;

    public RedisoperCommandImpl(LettuceResourceProvider lettuceResourceProvider) {
        command = lettuceResourceProvider.getCommand();
        asyncCommand = lettuceResourceProvider.getAsyncCommand();
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
        return (CompletableFuture<Boolean>)asyncCommand.expireat(key, timestamp);
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
        return ((CompletableFuture<String>) asyncCommand.setex(key, expire, value)).thenApply(this::isOk);
    }

    @Override
    public boolean setNx(String key, long expire, byte[] value) {
        return OK.equals(command.set(key, value, SetArgs.Builder.nx().ex(expire)));
    }

    @Override
    public List<KeyValue<String, byte[]>> mGet(List<String> keys) {
        return command.mget(keys.toArray(STRING_ARRAY_TMP));
    }

    // TODO 单节点Redis可使用lua脚本批量设置过期时间
    @Override
    public Boolean mSetEx(Map<String, byte[]> map, long expire) {
        String result = command.mset(map);
        map.forEach((k, v) -> asyncCommand.expire(k, expire));
        return isOk(result);
    }

    @Override
    public CompletableFuture<Boolean> mSetExAsync(Map<String, byte[]> map, long expire) {
        AsyncCommand<String, byte[], String> mset = (AsyncCommand<String, byte[], String>) asyncCommand.mset(map);
        return mset.thenApply(s -> {
            map.forEach((k, v) -> asyncCommand.expire(k, expire));
            return isOk(s);
        });
    }

    @Override
    public Long incrBy(String key, long increment) {
        return command.incrby(key, increment);
    }

    @Override
    public Long incrByUsingScript(String key, long increment, long expireSecond, String initValue) {
        return eval(INCRBY_SCRIPT, ScriptOutputType.INTEGER, new String[]{key},
                valueOf(increment).getBytes(), valueOf(expireSecond).getBytes(), valueOf(initValue).getBytes());
    }

    @Override
    public Long decrBy(String key, long decrement) {
        return command.decrby(key, decrement);
    }

    @Override
    public Long decrByUsingScript(String key, long decrement, long min, long expireSecond, String initValue) {
        return eval(DECRBY_SCRIPT, ScriptOutputType.INTEGER, new String[]{key},
                valueOf(decrement).getBytes(), valueOf(min).getBytes(), valueOf(expireSecond).getBytes(), valueOf(initValue).getBytes());
    }

    @Override
    public boolean cluster() {
        return false;
    }

    @Override
    public String scriptLoad(String script) {
        return command.scriptLoad(script.getBytes());
    }

    @Override
    public <T> T evalSha(String scriptSha, ScriptOutputType outputType, String[] keys, byte[]... args) {
        return command.evalsha(scriptSha, outputType, keys, args);
    }

    @Override
    public <T> T eval(String script, ScriptOutputType outputType, String[] keys, byte[]... args) {
        return command.eval(script, outputType, keys, args);
    }
}
