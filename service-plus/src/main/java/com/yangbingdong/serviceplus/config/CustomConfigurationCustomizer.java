package com.yangbingdong.serviceplus.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.handlers.MybatisEnumTypeHandler;
import org.apache.ibatis.session.Configuration;

/**
 * @author ybd
 * @date 2019/12/10
 * @contact yangbingdong1994@gmail.com
 */
public class CustomConfigurationCustomizer implements ConfigurationCustomizer {
    @Override
    public void customize(Configuration configuration) {
        configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
    }
}
