package com.yangbingdong.redisoperv2.core.command;

import com.yangbingdong.redisoperv2.Redisoperv2ApplicationTests;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 2019/11/28
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperCommandTest extends Redisoperv2ApplicationTests {
    @Autowired
    private RedisoperCommand command;

    private String key = "REDISOPER:TEST:BASIC";
    private byte[] value = "REDISOPER:TEST:BASIC:VALUE".getBytes();
    private long expire = 60;


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
        command.delAsync(key);
        Assertions.assertThat(command.setNx(key, expire, value))
                  .isTrue();
        command.delAsync(key);
    }

    @Test
    public void mGetAndMSetTest() {
        // command.mSet();
    }
}























