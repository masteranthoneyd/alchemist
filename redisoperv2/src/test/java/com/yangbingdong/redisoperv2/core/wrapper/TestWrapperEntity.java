package com.yangbingdong.redisoperv2.core.wrapper;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 2019/11/30
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class TestWrapperEntity {

    private Long id;

    private String name;
}
