package me.femrek.viewcounter.advice;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(name = "logging.level.me.femrek.viewcounter", havingValue = "TRACE")
@Log4j2
public class TraceLogger {
    @Around("execution(* me.femrek.viewcounter.controller.*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.trace("Entering method: {}", methodName);
        Object result = joinPoint.proceed();
        log.trace("Exiting method: {}", methodName);
        return result;
    }
}
