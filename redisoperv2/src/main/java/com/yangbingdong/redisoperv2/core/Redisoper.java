package com.yangbingdong.redisoperv2.core;

import com.yangbingdong.redisoperv2.core.command.RedisoperCommand;
import com.yangbingdong.redisoperv2.core.metadata.EntityMetadata;
import com.yangbingdong.redisoperv2.core.wrapper.SerializeWrapper;
import com.yangbingdong.redisoperv2.serializer.Serializer;
import io.lettuce.core.KeyValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;

/**
 * @author ybd
 * @date 2019/11/30
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class Redisoper<T> {

    public static final int EXPIRE = 7 * 24 * 60 * 60;
    @Getter
    private EntityMetadata<T> entityMetadata;
    private final Class<T> entityClass;

    @Autowired
    private RedisoperCommand command;
    @Autowired
    private Serializer serializer;

    private String noIndexMsg;

    public Redisoper(EntityMetadata<T> entityMetadata) {
        this.entityMetadata = entityMetadata;
        this.entityClass = entityMetadata.getEntityClass();
        this.noIndexMsg = entityMetadata.getEntityName() + " do not contain index";
    }

    public T getByKey(Supplier<T> dbLoader, Object... values) {
        return getByKey(dbLoader, true, values);
    }

    public T getByKey(Supplier<T> dbLoader, boolean cache, Object... values) {
        String primaryKey = entityMetadata.genRedisPrimaryKey(values);
        T entity = serializer.deserialize(command.get(primaryKey), entityClass);
        if (entity == null) {
            entity = dbLoader.get();
            if (cache && entity != null) {
                set2Redis(entity, primaryKey);
            }
        }
        return entity;
    }

    public T getByUniqueIndex(Supplier<T> dbLoader, String uniqueIndex, Object... values) {
        checkContainIndex();
        String redisUniqueIndexKey = entityMetadata.genRedisUniqueIndexKey(uniqueIndex, values);
        T entity;
        byte[] rawPrimaryKey = command.get(redisUniqueIndexKey);
        if (rawPrimaryKey != null) {
            String primaryKey = new String(rawPrimaryKey);
            entity = serializer.deserialize(command.get(primaryKey), entityClass);
            if (entity != null) {
                return entity;
            }
        }
        entity = dbLoader.get();
        if (entity != null) {
            String primaryKey = entityMetadata.genRedisPrimaryKeyByEntity(entity);
            set2Redis(entity, primaryKey);
            command.setExAsync(redisUniqueIndexKey, EXPIRE, primaryKey.getBytes());
            return entity;
        }
        return null;
    }

    public List<T> getByNormalIndex(Supplier<List<T>> dbLoader, String normalIndex, Object... values) {
        checkContainIndex();
        String normalRedisIndexKey = entityMetadata.genRedisNormalRedisIndexKey(normalIndex, values);
        SerializeWrapper wrapper = serializer.deserialize(command.get(normalRedisIndexKey), SerializeWrapper.class);
        if (wrapper != null) {
            List<KeyValue<String, byte[]>> kvs = command.mGet(wrapper.getList(String.class));
            if (kvs != null && !kvs.isEmpty()) {
                boolean dirty = false;
                List<T> entities = new ArrayList<>(kvs.size());
                for (KeyValue<String, byte[]> kv : kvs) {
                    T entity = serializer.deserialize(kv.getValue(), entityClass);
                    if (entity == null) {
                        dirty = true;
                        break;
                    }
                    entities.add(entity);
                }
                if (!dirty) {
                    return entities;
                }
            }
        }
        List<T> entities = dbLoader.get();
        if (entities != null && !entities.isEmpty()) {
            Map<String, byte[]> map = entities.stream().collect(toMap(entityMetadata::genRedisPrimaryKeyByEntity, serializer::serialize));
            command.mSetExAsync(map, EXPIRE);
            command.setExAsync(normalRedisIndexKey, EXPIRE, serializer.serialize(SerializeWrapper.of(new ArrayList<>(map.keySet()))));
            return entities;
        }
        return emptyList();
    }

    private void checkContainIndex() {
        Assert.isTrue(entityMetadata.isContainIndex(), noIndexMsg);
    }

    public void del(T entity) {
        if (entity == null) {
            return;
        }
        command.delAsync(entityMetadata.genRedisPrimaryKeyByEntity(entity));
        delIndex(entity);
    }

    public void delIndex(T entity) {
        if (entityMetadata.isContainIndex()) {
            String[] keys = (String[]) entityMetadata.getEntityIndexes()
                                                     .stream()
                                                     .map(e -> e.genRedisIndexKeyByEntity(entity))
                                                     .toArray();
            command.delAsync(keys);
        }
    }

    private void set2Redis(T entity, String primaryKey) {
        command.setExAsync(primaryKey, EXPIRE, serializer.serialize(entity));
    }
}
