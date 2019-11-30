package com.yangbingdong.redisoperv2.core.command;

import com.yangbingdong.redisoperv2.Redisoperv2ApplicationTests;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * @author ybd
 * @date 2019/11/30
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperSetCommandTest extends Redisoperv2ApplicationTests {

    @Autowired
    private RedisoperCommand command;
    private String key1 = "SADDTEST:KEY1";
    private String key2 = "SADDTEST:KEY2";
    private byte[] members = "1".getBytes();

    @Test
    public void sAddAndBatchExpireTest() {
        Long result = command.sAdd(key1, members);
        Assertions.assertThat(result)
                  .isEqualTo(1L);
        result = command.sAdd(key2, members);
        Assertions.assertThat(result)
                  .isEqualTo(1L);

        CompletableFuture<Boolean> f = command.batchExpireUsingLua(Arrays.asList(key1, key2), 60L);
        Assertions.assertThat(f.join())
                  .isTrue();
        Assertions.assertThat(command.ttl(key1))
                  .isGreaterThan(50L);
        Assertions.assertThat(command.ttl(key2))
                  .isGreaterThan(50L);
    }

    @After
    public void after() {
        command.del(key1, key2);
    }
}
