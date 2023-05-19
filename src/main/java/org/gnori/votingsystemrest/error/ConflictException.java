package org.gnori.votingsystemrest.error;

import org.springframework.http.HttpStatus;

public class ConflictException extends BusinessException{

  public ConflictException(String message, HttpStatus status) {
    super(message, status);
  }
}
