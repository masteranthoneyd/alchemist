package com.youngbingdong.redisoper.extend.dlock.annotated;

import com.youngbingdong.redisoper.extend.dlock.DLock;
import com.youngbingdong.util.spring.spel.SpringExpressionEvaluator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.lang.reflect.Method;


/**
 * @author ybd
 * @date 18-8-2
 * @contact yangbingdong1994@gmail.com
 */
@Aspect
public class DLockAspect implements ApplicationContextAware {
	@Resource
	private DLock dLock;

	@Value("${spring.application.name:default}")
	private String namespace;

    private ApplicationContext applicationContext;

    @Around(value = "@annotation(lock)")
	public Object doAround(ProceedingJoinPoint pjp, Lock lock) throws Throwable {
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		Object[] args = pjp.getArgs();
		String expression = lock.expression();
        Object target = pjp.getTarget();
        String resourceKey = SpringExpressionEvaluator.evalAsText(expression, method, args, target, target.getClass(), applicationContext);

		String finalKey = buildFinalKey(resourceKey);
		return dLock.tryLock(finalKey, lock.waitSecond(), lock.leaseSecond(), () -> pjp.proceed(pjp.getArgs()));
	}

	private String buildFinalKey(String key) {
		return "DLock:" + namespace + ":" + key;
	}


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
