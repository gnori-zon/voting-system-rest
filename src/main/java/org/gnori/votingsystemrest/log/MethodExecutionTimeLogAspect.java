package org.gnori.votingsystemrest.log;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Log4j2
@Component
public class MethodExecutionTimeLogAspect {

  private static final String DEBUG_TEXT_EXECUTION_TIME = "Method: |{}| executed in {} ms";

  @Around("@annotation(org.gnori.votingsystemrest.log.annotation.LogMethodExecutionTime)")
  public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{

    var startTime = System.currentTimeMillis();

    var executionResult = joinPoint.proceed();

    var executionTime = System.currentTimeMillis() - startTime;

    log.debug(DEBUG_TEXT_EXECUTION_TIME, joinPoint.getSignature(),  executionTime);

    return executionResult;

  }

}
