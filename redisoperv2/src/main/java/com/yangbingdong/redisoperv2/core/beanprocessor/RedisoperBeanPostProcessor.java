package com.yangbingdong.redisoperv2.core.beanprocessor;


import com.yangbingdong.redisoperv2.core.RedisoperAware;

import static com.youngbingdong.util.reflect.TypeUtil.getClassFromGenericTypeInterface;

/**
 * @author ybd
 * @date 19-3-24
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperBeanPostProcessor extends AbstractBeanPostProcessor {

	@Override
	Class resolveEntityClass(Object bean) {
		return getClassFromGenericTypeInterface(bean.getClass(), RedisoperAware.class);
	}

}
