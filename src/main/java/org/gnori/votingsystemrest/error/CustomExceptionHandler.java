package org.gnori.votingsystemrest.error;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ResponseBody
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAuthenticationException(Exception ex) {

    HttpStatus status;
    if (ex instanceof BusinessException businessException) {
      status = businessException.getStatus();
    } else if (ex instanceof AuthenticationException) {
      status = HttpStatus.FORBIDDEN;
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    return ResponseEntity
        .status(status)
        .body(ErrorDto.builder()
            .error(status.getReasonPhrase())
            .errorDescription(ex.getMessage())
            .build());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,@NonNull HttpStatusCode status,@NonNull WebRequest request) {

    var message = "Invalid request content.";

    Map<String, String> errorsMap = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> errorsMap.put(error.getField(), error.getDefaultMessage())
        );

    var error = ErrorDto.builder()
        .error(ex.getBody().getDetail())
        .error(message)
        .build();

    if (!errorsMap.isEmpty()) {
      error.setUserError(errorsMap);
    }

    return ResponseEntity
        .status(status)
        .headers(headers)
        .body(error);

  }
}