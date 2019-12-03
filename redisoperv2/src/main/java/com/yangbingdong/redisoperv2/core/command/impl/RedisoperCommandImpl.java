package com.yangbingdong.redisoperv2.core.command.impl;

import com.yangbingdong.redisoperv2.LettuceResourceProvider;
import com.yangbingdong.redisoperv2.core.command.RedisoperCommand;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.KeyValue;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.protocol.AsyncCommand;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static io.lettuce.core.ScanArgs.Builder.limit;
import static io.lettuce.core.ScanCursor.FINISHED;
import static io.lettuce.core.ScanCursor.INITIAL;
import static io.lettuce.core.ScriptOutputType.VALUE;
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
    private Charset charset = StandardCharsets.UTF_8;

    public RedisoperCommandImpl(LettuceResourceProvider lettuceResourceProvider) {
        command = lettuceResourceProvider.getCommand();
        asyncCommand = lettuceResourceProvider.getAsyncCommand();
    }

    @Override
    public boolean cluster() {
        return false;
    }

    /*########## Key ##########*/

    @Override
    public Long del(String... keys) {
        return command.del(keys);
    }

    @Override
    public void delByScan(String pattern) {
        ScanArgs scanArgs = limit(BATCH_SIZE).match(pattern);
        ScanCursor cursor = INITIAL;
        do {
            KeyScanCursor<String> result = command.scan(cursor, scanArgs);
            cursor = ScanCursor.of(result.getCursor());
            cursor.setFinished(result.isFinished());
            List<String> values = result.getKeys();
            if (!values.isEmpty()) {
                command.del(values.toArray(STRING_ARRAY_TMP));
            }
        } while (!(FINISHED.getCursor().equals(cursor.getCursor()) && FINISHED.isFinished() == cursor.isFinished()));
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
    public CompletableFuture<Boolean> batchExpireUsingLua(List<String> keys, long second) {
        return ((CompletableFuture<byte[]>) asyncCommand
                .<byte[]>eval(BATCH_EXPIRE, VALUE, keys.toArray(STRING_ARRAY_TMP), valueOf(keys.size()).getBytes(charset), valueOf(second).getBytes(charset)))
                .thenApply(b -> isOk(new String(b)));
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

    @Override
    public Boolean mSetEx(Map<String, byte[]> map, long expire) {
        String result = command.mset(map);
        batchExpireUsingLua(new ArrayList<>(map.keySet()), expire);
        return isOk(result);
    }

    @Override
    public CompletableFuture<Boolean> mSetExAsync(Map<String, byte[]> map, long expire) {
        AsyncCommand<String, byte[], String> mset = (AsyncCommand<String, byte[], String>) asyncCommand.mset(map);
        return mset.thenApply(s -> {
            batchExpireUsingLua(new ArrayList<>(map.keySet()), expire);
            return isOk(s);
        });
    }

    @Override
    public Long incrBy(String key, long increment) {
        return command.incrby(key, increment);
    }

    @Override
    public Long incrByUsingLua(String key, long increment, long expireSecond, String initValue) {
        return eval(INCRBY_SCRIPT, ScriptOutputType.INTEGER, new String[]{key},
                valueOf(increment).getBytes(charset), valueOf(expireSecond).getBytes(charset), valueOf(initValue).getBytes(charset));
    }

    @Override
    public Long decrBy(String key, long decrement) {
        return command.decrby(key, decrement);
    }

    @Override
    public Long decrByUsingLua(String key, long decrement, long min, long expireSecond, String initValue) {
        return eval(DECRBY_SCRIPT, ScriptOutputType.INTEGER, new String[]{key},
                valueOf(decrement).getBytes(charset), valueOf(min).getBytes(charset), valueOf(expireSecond).getBytes(charset), valueOf(initValue).getBytes(charset));
    }

    /*########## Scripting ##########*/

    @Override
    public String scriptLoad(String script) {
        return command.scriptLoad(script.getBytes(charset));
    }

    @Override
    public <T> T evalSha(String scriptSha, ScriptOutputType outputType, String[] keys, byte[]... args) {
        return command.evalsha(scriptSha, outputType, keys, args);
    }

    @Override
    public <T> T eval(String script, ScriptOutputType outputType, String[] keys, byte[]... args) {
        return command.eval(script, outputType, keys, args);
    }

    /*########## Set ##########*/

    @Override
    public Long sAdd(String key, byte[]... members) {
        return command.sadd(key, members);
    }
}
