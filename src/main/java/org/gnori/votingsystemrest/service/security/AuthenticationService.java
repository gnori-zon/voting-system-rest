package org.gnori.votingsystemrest.service.security;

public interface AuthenticationService<D, ID, T> {

  void authenticate(D data);
  void validatePermission(ID id, T token);
  T generateNewToken(D data);
}
