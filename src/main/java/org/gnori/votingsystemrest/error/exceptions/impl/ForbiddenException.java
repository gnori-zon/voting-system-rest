package org.gnori.votingsystemrest.error.exceptions.impl;

import org.gnori.votingsystemrest.error.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException {

  public ForbiddenException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }
}
