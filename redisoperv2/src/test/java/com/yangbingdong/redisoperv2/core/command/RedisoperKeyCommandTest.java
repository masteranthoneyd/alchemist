package com.yangbingdong.redisoperv2.core.command;

import com.yangbingdong.redisoperv2.Redisoperv2ApplicationTests;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

/**
 * @author ybd
 * @date 2019/11/28
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperKeyCommandTest extends Redisoperv2ApplicationTests {
    @Autowired
    private RedisoperCommand command;

    private String key1 = "REDISOPER:TEST:BASIC";
    private byte[] value1 = "REDISOPER:TEST:BASIC:VALUE".getBytes();

    private long expire = 60;

    @Test
    public void delTest() {
        command.setEx(key1, expire, value1);
        Assertions.assertThat(command.del(key1))
                  .isEqualTo(1L);
    }

    @Test
    public void delAsyncTest() {
        command.setEx(key1, expire, value1);
        CompletableFuture<Long> f = command.delAsync(key1);
        Assertions.assertThat(f.join())
                  .isEqualTo(1L);
    }

    @Test
    public void unlinkAsyncTest() {
        command.setEx(key1, expire, value1);
        CompletableFuture<Long> f = command.unlinkAsync(key1);
        Assertions.assertThat(f.join())
                  .isEqualTo(1L);
    }

    @Test
    public void existsTest() {
        command.setEx(key1, expire, value1);
        Assertions.assertThat(command.exists(key1))
                  .isTrue();
    }

    @Test
    public void expireAsyncTest() {
        command.setEx(key1, expire, value1);
        Assertions.assertThat(command.expireAsync(key1, 10L).join())
                  .isTrue();
        command.del(key1);
        Assertions.assertThat(command.expireAsync(key1, 10L).join())
                  .isFalse();
    }

    @Test
    public void expireAtAsyncTest() {
        command.setEx(key1, expire, value1);
        Assertions.assertThat(command.expireAtAsync(key1, System.currentTimeMillis() + 10L).join())
                  .isTrue();
        command.del(key1);
        Assertions.assertThat(command.expireAtAsync(key1, System.currentTimeMillis() + 10L).join())
                  .isFalse();
    }

    @Test
    public void ttlTest() {
        command.setEx(key1, expire, value1);
        Assertions.assertThat(command.ttl(key1))
                  .isGreaterThan(50L);
        command.del(key1);
        Assertions.assertThat(command.ttl(key1))
                  .isEqualTo(-2L);
    }

    @After
    public void clean() {
        command.del(key1);
    }
}




















