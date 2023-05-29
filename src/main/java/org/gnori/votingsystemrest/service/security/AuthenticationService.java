package org.gnori.votingsystemrest.service.security;

import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.error.ForbiddenException;
import org.gnori.votingsystemrest.model.auth.LoginDetails;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final JwtService jwtService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  public LoginDetails authenticateAndUpdateToken(UserDto userDto) {

    authenticate(userDto);

    var token = generateNewToken(userDto);

    return LoginDetails.builder()
        .token(token)
        .build();

  }

  public String getUsername(String token) {

    return jwtService.extractUsername(token);

  }

  public String generateNewToken(UserDto userDto){

    var user = userService.getByUsername(userDto.getUsername());

    return jwtService.generateToken(user);

  }

  private void authenticate(UserDto userDto) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userDto.getUsername(),
            userDto.getPassword()
        )
    );

  }

  public void validatePermission(Integer userId, String token) {

    var user = userService.getUserDtoById(userId);

    var usernameFromToken = getUsername(token);

    if (!usernameFromToken.equals(user.getUsername())) {
      throw new ForbiddenException(String.format(
          "You do not have rights to get or change information about the user with id: %d",
          userId
      ));
    }

  }

}
