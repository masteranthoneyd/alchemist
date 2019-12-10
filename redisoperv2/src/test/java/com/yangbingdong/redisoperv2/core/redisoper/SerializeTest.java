package com.yangbingdong.redisoperv2.core.redisoper;

import com.yangbingdong.redisoperv2.core.redisoper.entity.TestUser;
import com.yangbingdong.redisoperv2.serializer.ProtostuffSerializer;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author ybd
 * @date 2019/12/9
 * @contact yangbingdong1994@gmail.com
 */
public class SerializeTest {

    private ProtostuffSerializer serializer = new ProtostuffSerializer();

    @Test
    public void serializeTest() {
        TestUser user = TestUser.buildUserByName("ybd");
        user.setTransientField("忽略字段");
        byte[] bytes = serializer.serialize(user);
        user = serializer.deserialize(bytes, TestUser.class);
        Assertions.assertThat(user.getTransientField())
                  .isNull();
    }
}
