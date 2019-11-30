package com.yangbingdong.redisoperv2.core.command;

import com.yangbingdong.redisoperv2.Redisoperv2ApplicationTests;
import io.lettuce.core.ScriptOutputType;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 2019/11/29
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperScriptingCommandTest extends Redisoperv2ApplicationTests {

    @Autowired
    private RedisoperCommand command;

    private String key = "REDISOPER:TEST:SCRIPTING";
    private String key2 = "REDISOPER:TEST:SCRIPTING";
    private byte[] value = "REDISOPER:TEST:BASIC:SCRIPTING".getBytes();

    @Test
    public void loadAndEvalShaTest() {
        String load = command.scriptLoad("return redis.call('SET',KEYS[1], ARGV[1])");
        byte[] o = command.evalSha(load, ScriptOutputType.VALUE, new String[]{key}, value);
        Assertions.assertThat(new String(o))
                  .isEqualTo("OK");
        Assertions.assertThat(command.get(key))
                  .isEqualTo(value);
    }

    @Test
    public void decrByUsingScriptTest() {
        Long result = command.decrByUsingLua(key, 1, 0, 60, "2");
        Assertions.assertThat(result)
                  .isEqualTo(1);
        result = command.decrByUsingLua(key, 1, 0, 60, "2");
        Assertions.assertThat(result)
                  .isEqualTo(0);
        result = command.decrByUsingLua(key, 1, 0, 60, "2");
        Assertions.assertThat(result)
                  .isEqualTo(-1);
        result = command.decrByUsingLua(key, 1, 0, 60, "2");
        Assertions.assertThat(result)
                  .isEqualTo(-1);
    }

    @Test
    public void incrByUsingScriptTest() {
        Long result = command.incrByUsingLua(key, 1, 60, "0");
        Assertions.assertThat(result)
                  .isEqualTo(1);
        result = command.incrByUsingLua(key, 1, 60, "0");
        Assertions.assertThat(result)
                  .isEqualTo(2);
    }

    @Test
    public void batchExpireTest() {
        // command
    }

    @After
    public void after() {
        command.del(key);
    }
}
