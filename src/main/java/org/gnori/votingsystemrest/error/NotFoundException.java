package org.gnori.votingsystemrest.error;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException{

  public NotFoundException(String message, HttpStatus status) {
    super(message, status);
  }
}
