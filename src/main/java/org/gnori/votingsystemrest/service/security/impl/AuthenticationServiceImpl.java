package org.gnori.votingsystemrest.service.security.impl;

import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.error.ForbiddenException;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.gnori.votingsystemrest.service.security.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService<UserDto, Integer, String> {

  private static final Integer BEGIN_INDEX_TOKEN = 7;

  private final JwtService jwtService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  @Override
  public void authenticate(UserDto userDto) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userDto.getUsername(),
            userDto.getPassword()
        )
    );

  }

  @Override
  public void validatePermission(Integer userId, String token) {

    var user = userService.getUserDtoById(userId);

    var usernameFromToken = getUsername(token.substring(BEGIN_INDEX_TOKEN));

    if (!usernameFromToken.equals(user.getUsername())) {
      throw new ForbiddenException(String.format(
          "You do not have rights to get or change information about the user with id: %d",
          userId
      ));
    }

  }

  @Override
  public String generateNewToken(UserDto userDto){

    var user = userService.getByUsername(userDto.getUsername());

    return jwtService.generateToken(user);

  }

  private String getUsername(String token) {

    return jwtService.extractUsername(token);

  }

}
