package com.yangbingdong.redisoperv2;

import com.yangbingdong.redisoperv2.config.RedisoperProperties;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.collection.CollUtil.isNotEmpty;
import static java.util.stream.Collectors.toList;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
public class LettuceResourceProvider {
    private AbstractRedisClient abstractRedisClient;
    @Getter
    private boolean cluster = false;
    private RedisCodec<String, byte[]> codec = RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE);
    private List<StatefulConnection> closableConnections = new ArrayList<>();

    public static LettuceResourceProvider create(RedisoperProperties redisoperProperties) {
        return new LettuceResourceProvider(redisoperProperties);
    }

    private LettuceResourceProvider(RedisoperProperties redisoperProperties) {
        RedisoperProperties.Client client = redisoperProperties.getClient();
        Assert.isTrue(client.getCluster() != null || client.getHost() != null, "Redisoper config not found");
        DefaultClientResources clientResources = DefaultClientResources.create();
        if (isNotEmpty(client.getCluster())) {
            cluster = true;
            abstractRedisClient = RedisClusterClient.create(clientResources, resoleRedisURIs(client));
        } else {
            String[] split = client.getHost().split(":");
            abstractRedisClient = RedisClient.create(clientResources, RedisURI.create(split[0], Integer.parseInt(split[1])));
        }
    }

    private List<RedisURI> resoleRedisURIs(RedisoperProperties.Client client) {
        return client.getCluster()
                     .stream()
                     .map(s -> {
                         String[] split = s.split(":");
                         return RedisURI.create(split[0], Integer.parseInt(split[1]));
                     })
                     .collect(toList());
    }

    public RedisAdvancedClusterCommands<String, byte[]> getClusterCommand() {
        if (!cluster) {
            throw new UnsupportedOperationException("Cluster Config not found");
        }
        RedisClusterClient clusterClient = (RedisClusterClient) abstractRedisClient;
        StatefulRedisClusterConnection<String, byte[]> connect = clusterClient.connect(codec);
        closableConnections.add(connect);
        return connect.sync();
    }

    public RedisAdvancedClusterAsyncCommands<String, byte[]> getClusterAsyncCommand() {
        if (!cluster) {
            throw new UnsupportedOperationException("Cluster Config not found");
        }
        RedisClusterClient clusterClient = (RedisClusterClient) abstractRedisClient;
        StatefulRedisClusterConnection<String, byte[]> connect = clusterClient.connect(codec);
        closableConnections.add(connect);
        return connect.async();
    }

    public RedisCommands<String, byte[]> getCommand() {
        if (cluster) {
            throw new UnsupportedOperationException("Please use getClusterCommand");
        }
        RedisClient redisClient = (RedisClient) abstractRedisClient;
        StatefulRedisConnection<String, byte[]> connect = redisClient.connect(codec);
        closableConnections.add(connect);
        return connect.sync();
    }

    public RedisAsyncCommands<String, byte[]> getAsyncCommand() {
        if (cluster) {
            throw new UnsupportedOperationException("Please use getClusterAsyncCommand");
        }
        RedisClient redisClient = (RedisClient) abstractRedisClient;
        StatefulRedisConnection<String, byte[]> connect = redisClient.connect(codec);
        closableConnections.add(connect);
        return connect.async();
    }

    public void shutdown() {
        closableConnections.forEach(StatefulConnection::close);
        abstractRedisClient.shutdown();
    }


}
