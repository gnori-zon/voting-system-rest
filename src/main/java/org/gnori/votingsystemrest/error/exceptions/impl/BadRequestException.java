package org.gnori.votingsystemrest.error.exceptions.impl;

import org.gnori.votingsystemrest.error.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException {

  public BadRequestException(String message, HttpStatus status) {
    super(message, status);
  }
}
