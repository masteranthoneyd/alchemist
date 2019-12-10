package com.yangbingdong.example.controller.vo.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.yangbingdong.service.core.EnumValueProvider;
import com.yangbingdong.service.util.FastJsonEnumCodec;

/**
 * @author ybd
 * @date 2019/12/9
 * @contact yangbingdong1994@gmail.com
 */
@JSONType(serializeEnumAsJavaBean = true, serializer = FastJsonEnumCodec.class, deserializer = FastJsonEnumCodec.class)
public enum  Gender implements EnumValueProvider {
    MALE(1,"男"),
    FEMALE(2,"女");

    private Integer id;
    private String name;
    Gender(Integer id,String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Gender setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Gender setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Integer getValue() {
        return id;
    }

}
