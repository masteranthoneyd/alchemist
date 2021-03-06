package com.yangbingdong.mvc.log.core;

import com.yangbingdong.mvc.disruptor.DisruptorEngine;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 19-5-6
 * @contact yangbingdong1994@gmail.com
 */
@Aspect
@Slf4j
public class OpLogAspect {

	@Autowired
	private DisruptorEngine disruptorEngine;

	@Autowired
	private OpLogContextBuilder opLogContextBuilder;

	@AfterReturning(value = "@annotation(opLog)", returning = "returnValue")
	public void doAfterReturning(JoinPoint joinPoint, OpLog opLog, Object returnValue) {
		Object opLogContext = opLogContextBuilder.buildContext(joinPoint, opLog, returnValue, null);
		disruptorEngine.publish(opLogContext);
	}

	@AfterThrowing(value = "@annotation(opLog)", throwing = "exception")
	public void doAfterThrowing(JoinPoint joinPoint, OpLog opLog, Throwable exception) {
		Object opLogContext = opLogContextBuilder.buildContext(joinPoint, opLog, null, exception);
		disruptorEngine.publish(opLogContext);
	}
}
