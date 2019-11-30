package com.yangbingdong.redisoperv2.core.wrapper;

import com.yangbingdong.redisoperv2.Redisoperv2ApplicationTests;
import com.yangbingdong.redisoperv2.core.command.RedisoperCommand;
import com.yangbingdong.redisoperv2.serializer.Serializer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 2019/11/30
 * @contact yangbingdong1994@gmail.com
 */
public class SerializeWrapperTest extends Redisoperv2ApplicationTests {

    @Autowired
    private Serializer serializer;

    @Autowired
    private RedisoperCommand command;

    @Test
    public void wrapperTest() {
        TestWrapperEntity entity = build();
        byte[] serialize = serializer.serialize(SerializeWrapper.of(entity));
        SerializeWrapper wrapper = serializer.deserialize(serialize, SerializeWrapper.class);
        Assertions.assertThat(wrapper.getData())
                  .isEqualTo(entity);
    }

    @Test
    public void getSetTest() {
        TestWrapperEntity entity = build();
        SerializeWrapper wrapper = SerializeWrapper.of(entity);
        String key = getKey(entity);
        command.setEx(key, 60L, serializer.serialize(wrapper));
        byte[] bytes = command.get(key);
        SerializeWrapper deserializeWrapper = serializer.deserialize(bytes, SerializeWrapper.class);
        Assertions.assertThat(deserializeWrapper.getData())
                  .isEqualTo(entity);

    }

    private String getKey(TestWrapperEntity entity) {
        return TestWrapperEntity.class.getSimpleName() + ":" + entity.getId();
    }

    private TestWrapperEntity build() {
        TestWrapperEntity entity = new TestWrapperEntity();
        return entity.setId(666L)
                     .setName("yangbingdong");
    }
}
