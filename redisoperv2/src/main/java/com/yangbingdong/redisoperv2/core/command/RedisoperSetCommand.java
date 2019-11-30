package com.yangbingdong.redisoperv2.core.command;

/**
 * @author ybd
 * @date 2019/11/30
 * @contact yangbingdong1994@gmail.com
 */
public interface RedisoperSetCommand {

    Long sAdd(String key, byte[]... members);
}
