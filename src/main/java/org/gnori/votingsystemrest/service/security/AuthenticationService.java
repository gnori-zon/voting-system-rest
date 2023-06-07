package org.gnori.votingsystemrest.service.security;

public interface AuthenticationService<D, ID, T> {

  void authenticate(D data);
  ID getUserIdFrom(String token) ;
  T generateNewToken(D data);
}
