package com.yangbingdong.redisoperv2.core;

import com.yangbingdong.redisoperv2.core.command.RedisoperCommand;
import com.yangbingdong.redisoperv2.core.metadata.EntityMetadata;
import com.yangbingdong.redisoperv2.serializer.Serializer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

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

    public Redisoper(EntityMetadata<T> entityMetadata) {
        this.entityMetadata = entityMetadata;
        this.entityClass = entityMetadata.getEntityClass();
    }

    public T getByKey(Supplier<T> dbLoader, Object... value) {
        String primaryKey = entityMetadata.genRedisPrimaryKey(value);
        /*T o = (T) RedisTransactionResourceHolder.get(primaryKey);
        if (o != null) {
            return o;
        }*/
        T entity = serializer.deserialize(command.get(primaryKey), entityClass);
        return loadFromDbAndSet2RedisIfNull(dbLoader, entity, primaryKey);
    }

    private T loadFromDbAndSet2RedisIfNull(Supplier<T> dbLoader, T entity, String primaryKey) {
        if (entity == null) {
            entity = dbLoader.get();
            if (entity != null) {
                set2Redis(entity, primaryKey);
            }
        }
        return entity;
    }


    public void del(T entity) {
        String key = entityMetadata.genRedisPrimaryKeyByEntity(entity);
        command.delAsync(key);
    }

    private void set2Redis(T entity, String primaryKey) {
        command.setExAsync(primaryKey, EXPIRE, serializer.serialize(entity));
    }

}
