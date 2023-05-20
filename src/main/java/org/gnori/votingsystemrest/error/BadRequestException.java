package org.gnori.votingsystemrest.error;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException {

  public BadRequestException(String message, HttpStatus status) {
    super(message, status);
  }
}
