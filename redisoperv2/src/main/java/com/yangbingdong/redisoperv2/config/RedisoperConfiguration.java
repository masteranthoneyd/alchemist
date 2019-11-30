package com.yangbingdong.redisoperv2.config;

import com.yangbingdong.redisoperv2.LettuceResourceProvider;
import com.yangbingdong.redisoperv2.core.command.RedisoperCommand;
import com.yangbingdong.redisoperv2.core.command.impl.RedisoperClusterCommandImpl;
import com.yangbingdong.redisoperv2.core.command.impl.RedisoperCommandImpl;
import com.yangbingdong.redisoperv2.serializer.ProtostuffSerializer;
import com.yangbingdong.redisoperv2.serializer.Serializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@EnableConfigurationProperties(RedisoperProperties.class)
public class RedisoperConfiguration {

    @Bean(destroyMethod = "shutdown")
    public LettuceResourceProvider lettuceResourceProvider(RedisoperProperties redisoperProperties) {
        return LettuceResourceProvider.create(redisoperProperties);
    }

    @Bean
    public RedisoperCommand redisoperCommand(LettuceResourceProvider provider) {
        return provider.isCluster() ? new RedisoperClusterCommandImpl(provider) : new RedisoperCommandImpl(provider);
    }

    @Bean
    public Serializer serializer() {
        return new ProtostuffSerializer();
    }

}
