package me.femrek.viewcounter.advice;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
public class ExecutionTimeAdvice {
    @Around("execution(* me.femrek.viewcounter.controller.*.*(..)) || execution(* me.femrek.viewcounter.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        String methodName = joinPoint.getSignature().getName();
        log.trace("Method {} executed in {}ms", methodName, executionTime);

        return proceed;
    }
}
