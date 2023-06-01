package org.gnori.votingsystemrest.log;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class ServiceLogAspect {

  private static final String ERROR_TEXT_SERVICE = "[{}] threw exception: {} with message '{}' from method({})";
  private static final String DEBUG_TEXT_SECURITY_SERVICE = "Exception: {} - message: {} \n stack trace: {}";

  @AfterThrowing(pointcut = "execution(* org.gnori.votingsystemrest.service.impl.*.*(..))", throwing = "exception")
  public void afterThrowingExceptionFromServiceMethods(Exception exception) {

    var methodName = "unknown method";
    var serviceName = "unknown service";
    var stackTrace = exception.getStackTrace();
    var exceptionMessage = exception.getMessage();
    var exceptionName = exception.getClass().getSimpleName();

    if (stackTrace != null && stackTrace.length > 0 && stackTrace[0] != null) {
      serviceName = stackTrace[0].getClassName();
      methodName = stackTrace[0].getMethodName();
    }

    log.error(ERROR_TEXT_SERVICE, serviceName, exceptionName, exceptionMessage, methodName);
    log.debug(DEBUG_TEXT_SECURITY_SERVICE, exceptionName, exceptionMessage, stackTrace);

  }

}
