package com.yangbingdong.redisoperv2.core.command;

import cn.hutool.core.map.MapUtil;
import com.yangbingdong.redisoperv2.Redisoperv2ApplicationTests;
import io.lettuce.core.KeyValue;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author ybd
 * @date 2019/11/28
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperStringCommandTest extends Redisoperv2ApplicationTests {
    @Autowired
    private RedisoperCommand command;

    private String key = "REDISOPER:TEST:BASIC";
    private String key2 = "REDISOPER:TEST:BASIC2";
    private byte[] value = "REDISOPER:TEST:BASIC:VALUE".getBytes();
    private long expire = 60;

    @Test
    public void getAndSetExTest() {
        command.setEx(key, expire, value);
        Assertions.assertThat(command.get(key))
                  .isEqualTo(value);
        Assertions.assertThat(command.ttl(key))
                  .isGreaterThan(50);
        command.del(key);
        CompletableFuture<Boolean> f = command.setExAsync(key, 60, value);
        Assertions.assertThat(f.join())
                  .isTrue();
    }


    @Test
    public void setExNxTest() throws InterruptedException {
        command.setEx(key, expire, value);
        Thread.sleep(1L);
        Assertions.assertThat(command.ttl(key))
                  .isGreaterThan(50);
        command.delAsync(key);

        Assertions.assertThat(command.setNx(key, expire, value))
                  .isTrue();
        Assertions.assertThat(command.ttl(key))
                  .isGreaterThan(50);
        Assertions.assertThat(command.setNx(key, expire, value))
                  .isFalse();
        command.delAsync(key);

        Assertions.assertThat(command.setNx(key, expire, value))
                  .isTrue();
        Assertions.assertThat(command.ttl(key))
                  .isGreaterThan(50);
        CompletableFuture<Boolean> f = command.delAsync(key)
                                              .thenApply(l -> command.setNx(key, expire, value));
        Assertions.assertThat(f.join())
                  .isTrue();
        command.delAsync(key);
    }

    @Test
    public void mGetTest() {
        command.setEx(key, 60, value);
        command.setEx(key2, 60, value);
        List<KeyValue<String, byte[]>> keyValues = command.mGet(Arrays.asList(key, key2));
        Assertions.assertThat(keyValues)
                  .hasSize(2);
        Assertions.assertThat(keyValues.get(0).getValue())
                  .isEqualTo(value);
        Assertions.assertThat(keyValues.get(1).getValue())
                  .isEqualTo(value);
    }

    @Test
    public void mSetExTest() {
        Map<String, byte[]> map = MapUtil.builder(key, value)
                                           .put(key2, value)
                                           .build();
        Boolean result = command.mSetEx(map, 60L);
        Assertions.assertThat(result)
                  .isTrue();
        Assertions.assertThat(command.ttl(key2))
                  .isGreaterThan(50);
        command.del(key, key2);
        CompletableFuture<Boolean> f = command.mSetExAsync(map, 60L);
        Assertions.assertThat(f.join())
                  .isTrue();
        Assertions.assertThat(command.ttl(key2))
                  .isGreaterThan(50);

    }

    @After
    public void clean() {
        command.del(key, key2);
    }
}























