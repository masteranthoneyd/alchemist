package com.yangbingdong.serviceplus.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yangbingdong.serviceplus.enums.AgeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class CommonUser {
    @TableId(type = IdType.INPUT)
    private Long id;

    private String name;

    private String email;

    /**
     * IEnum接口的枚举处理
     */
    private AgeEnum age;

    private boolean deleted;

}
