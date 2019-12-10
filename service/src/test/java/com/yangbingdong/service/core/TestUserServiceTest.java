package com.yangbingdong.service.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ybd
 * @date 2019/12/9
 * @contact yangbingdong1994@gmail.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUserServiceTest {

    @Autowired
    private TestUserService testUserService;

    @Test
    public void test() {
        System.out.println(testUserService.getClass().getSimpleName());
    }
}
