package com.yangbingdong.serviceplus.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;

/**
 * @author ybd
 * @date 2019/12/10
 * @contact yangbingdong1994@gmail.com
 */
public class CustomMybatisPlusPropertiesCustomizer implements MybatisPlusPropertiesCustomizer {
    @Override
    public void customize(MybatisPlusProperties properties) {
        properties.getGlobalConfig().getDbConfig().setLogicDeleteField("deleted");
    }
}
