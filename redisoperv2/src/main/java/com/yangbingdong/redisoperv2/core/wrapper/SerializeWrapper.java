package com.yangbingdong.redisoperv2.core.wrapper;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class SerializeWrapper {
    private Object data;
    private long expireTime;


    public static SerializeWrapper of(Object data) {
        return new SerializeWrapper().setData(data);
    }

    public static SerializeWrapper of(Object data, long expireTime) {
        return new SerializeWrapper().setData(data).setExpireTime(expireTime);
    }


}

