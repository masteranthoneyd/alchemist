package com.yangbingdong.redisoperv2.core.redisoper;

import com.yangbingdong.redisoperv2.Redisoperv2ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 2019/11/30
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperTest extends Redisoperv2ApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        userService.getById(666L);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            userService.getById(666L);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

}
