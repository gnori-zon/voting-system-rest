package org.gnori.votingsystemrest.unit.service;

import org.gnori.votingsystemrest.error.exceptions.impl.ForbiddenException;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.gnori.votingsystemrest.service.security.impl.AuthenticationServiceImpl;
import org.gnori.votingsystemrest.service.security.impl.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@DisplayName("Unit test for AuthenticationServiceImplTest")
class AuthenticationServiceImplTest {

  UserService userServiceMock = Mockito.mock(UserService.class);
  JwtService jwtServiceMock = Mockito.mock(JwtService.class);
  AuthenticationManager authManagerMock = Mockito.mock(AuthenticationManager.class);

  AuthenticationServiceImpl service =
      new AuthenticationServiceImpl(jwtServiceMock, userServiceMock, authManagerMock);

  @Test
  void authenticate() {

    String username = "user";
    String password = "pass";
    var rawUserDto = UserDto.builder().username(username).password(password).build();

    service.authenticate(rawUserDto);

    Mockito.verify(authManagerMock).authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
    );

  }

  @Test
  void validatePermissionSuccess() {

    var id = 1;
    var token = "*******TOKEN";
    var indexBeginToken = 7;
    var username = "user";
    var userDto = UserDto.builder().username(username).build();

    Mockito.when(userServiceMock.getUserDtoById(id)).thenReturn(userDto);
    Mockito.when(jwtServiceMock.extractUsername(token.substring(indexBeginToken)))
        .thenReturn(username);

    service.validatePermission(id, token);

  }

  @Test
  void validatePermissionShouldThrowForbiddenException() {

    var id = 1;
    var token = "*******TOKEN";
    var indexBeginToken = 7;
    var username = "user";
    var userDto = UserDto.builder().username(username).build();

    Mockito.when(userServiceMock.getUserDtoById(id)).thenReturn(userDto);
    Mockito.when(jwtServiceMock.extractUsername(token.substring(indexBeginToken)))
        .thenReturn("otherUsername");

    Assertions.assertThrows(
        ForbiddenException.class,
        () -> service.validatePermission(id, token)
    );

  }

  @Test
  void generateNewToken() {

    var username = "user";
    var token = "token";
    var userDto = UserDto.builder().username(username).build();
    var userEntity = UserEntity.builder().build();

    Mockito.when(userServiceMock.getByUsername(username)).thenReturn(userEntity);
    Mockito.when(jwtServiceMock.generateToken(userEntity)).thenReturn(token);

    Assertions.assertEquals(token, service.generateNewToken(userDto));

  }

}
