package org.gnori.votingsystemrest.service.security;

import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserDao userDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    return userDao.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException(
                String.format("user with name: %s not found", username)
            )
    );

  }
}
