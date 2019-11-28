package com.yangbingdong.redisoperv2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
@Data
@ConfigurationProperties(RedisoperProperties.PREFIX)
public class RedisoperProperties {
    public static final String PREFIX = "alchemist.redisoper";

    private Client client = new Client();

    @Data
    @EnableConfigurationProperties
    public static class Client {
        private String host;
        private List<String> cluster;
    }
}
