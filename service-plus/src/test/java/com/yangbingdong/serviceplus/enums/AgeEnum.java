package com.yangbingdong.serviceplus.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
public enum AgeEnum implements IEnum<Integer> {
  ONE(1, "一岁"),
  TWO(2, "二岁"),
  THREE(3, "三岁");

  private int value;
  private String desc;

  AgeEnum(final int value, final String desc) {
    this.value = value;
    this.desc = desc;
  }

  @Override
  public Integer getValue() {
    return value;
  }
}
