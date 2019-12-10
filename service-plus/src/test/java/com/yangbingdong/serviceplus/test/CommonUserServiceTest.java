package com.yangbingdong.serviceplus.test;

import com.yangbingdong.serviceplus.ServicePlusApplicationTests;
import com.yangbingdong.serviceplus.domain.entity.CommonUser;
import com.yangbingdong.serviceplus.domain.servive.CommonUserService;
import com.yangbingdong.serviceplus.enums.AgeEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

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
        Assertions.assertThat(user)
                  .isNotNull()
                  .extracting(CommonUser::getId)
                  .isEqualTo(1L);
    }

    @Test
    public void saveTest() {
        CommonUser user = new CommonUser();
        user.setId(10000L)
            .setAge(AgeEnum.THREE)
            .setEmail("yangbigndong.com")
            .setName("yangbingdong");
        Assertions.assertThat(commonUserService.save(user))
                  .isTrue();
        Assertions.assertThatExceptionOfType(DuplicateKeyException.class)
                  .isThrownBy(() -> commonUserService.save(user));
    }
}
