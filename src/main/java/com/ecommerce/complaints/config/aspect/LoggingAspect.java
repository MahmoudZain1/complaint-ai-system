package com.ecommerce.complaints.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @Pointcut("@annotation(com.ecommerce.complaints.config.aspect.annotation.LogClass) || @within(com.ecommerce.complaints.config.aspect.annotation.LogClass)")
    public void logPointcut() {}


    @Around("logPointcut()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable{

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("START | Method: {}.{} | Args: {}", className, methodName, Arrays.toString(args));
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.info("SUCCESS: {}.{} | Return: {} | Took: {}ms", className, methodName, result, elapsed);
            return result;
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("ERROR in {}.{} | Time: {}ms | Message: {}", className, methodName, elapsed, e.getMessage(), e);
            throw e;
        }
    }
}
