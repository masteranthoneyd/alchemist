package com.yangbingdong.serviceplus.test;

import com.yangbingdong.serviceplus.ServicePlusApplicationTests;
import com.yangbingdong.serviceplus.domain.entity.CommonUser;
import com.yangbingdong.serviceplus.domain.servive.CommonUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 2019/12/10
 * @contact yangbingdong1994@gmail.com
 */
public class CommonUserServiceTest extends ServicePlusApplicationTests {
    @Autowired
    private CommonUserService commonUserService;

    @Test
    public void getByIdTest() {
        CommonUser user = commonUserService.getById(1L);
        System.out.println(user);
    }
}
