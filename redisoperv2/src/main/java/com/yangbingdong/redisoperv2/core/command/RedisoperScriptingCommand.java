package com.yangbingdong.redisoperv2.core.command;

import io.lettuce.core.ScriptOutputType;

/**
 * @author ybd
 * @date 2019/11/29
 * @contact yangbingdong1994@gmail.com
 */
public interface RedisoperScriptingCommand {

    String DECRBY_SCRIPT = "local decr = redis.call('GET',KEYS[1])\n" +
            "if decr == false then\n" +
            "redis.call('SETEX',KEYS[1], ARGV[3], ARGV[4])\n" +
            "decr = ARGV[4]\n" +
            "end\n" +
            "if tonumber(decr) - tonumber(ARGV[1]) < tonumber(ARGV[2]) then\n" +
            "return -1;\n" +
            "end\n" +
            "return redis.call('DECRBY',KEYS[1],ARGV[1])";

    String INCRBY_SCRIPT = "local incr = redis.call('GET',KEYS[1])\n" +
            "if incr == false then\n" +
            "redis.call('SETEX',KEYS[1], ARGV[2], ARGV[3])\n" +
            "end\n" +
            "return redis.call('INCRBY',KEYS[1],ARGV[1])";

    String scriptLoad(String script);

    <T> T evalSha(String scriptSha, ScriptOutputType outputType, String[] keys, byte[]... args);

    <T> T eval(String script, ScriptOutputType outputType, String[] keys, byte[]... args);
}
