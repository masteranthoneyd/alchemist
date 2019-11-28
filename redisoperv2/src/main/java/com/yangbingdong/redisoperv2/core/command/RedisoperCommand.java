package com.yangbingdong.redisoperv2.core.command;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
public interface RedisoperCommand extends
        RedisoperKeyCommand,
        RedisoperStringCommand {

    Long LONG_ONE = 1L;

    String OK = "OK";

    default boolean isOk(String result){
        return OK.equals(result);
    }

    String[] STRING_ARRAY_TMP = new String[0];
}
