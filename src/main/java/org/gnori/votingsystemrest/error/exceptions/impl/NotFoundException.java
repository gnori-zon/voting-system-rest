package org.gnori.votingsystemrest.error.exceptions.impl;

import org.gnori.votingsystemrest.error.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {

  public NotFoundException(String message, HttpStatus status) {
    super(message, status);
  }

}
