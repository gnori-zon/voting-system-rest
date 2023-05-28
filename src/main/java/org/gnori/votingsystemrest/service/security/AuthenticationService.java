package org.gnori.votingsystemrest.service.security;

import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.model.auth.LoginDetails;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final JwtService jwtService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  public LoginDetails register(UserDto userDto) {

    var user = userService.createFromUserDto(userDto);

    var token = jwtService.generateToken(user);

    return LoginDetails.builder()
        .token(token)
        .build();

  }

  public LoginDetails authenticateAndUpdateToken(UserDto userDto) {

    authenticate(userDto);

    var user = userService.getByUsername(userDto.getUsername());

    var token = jwtService.generateToken(user);

    return LoginDetails.builder()
        .token(token)
        .build();

  }

  public String getUsername(String token) {

    return jwtService.extractUsername(token);

  }

  public void authenticate(UserDto userDto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userDto.getUsername(),
            userDto.getPassword()
        )
    );
  }

}
