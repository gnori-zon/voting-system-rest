package org.gnori.votingsystemrest.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.model.auth.LoginDetails;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.gnori.votingsystemrest.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

  public static final String USER_URL = "/users";
  public static final String USER_WITH_ID_URL = USER_URL + "/{userId}";
  public static final String AUTH_URL = "/auth";

  UserService userService;
  AuthenticationService authenticationService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(USER_WITH_ID_URL)
  public UserDto get(@PathVariable Integer userId, @RequestBody LoginDetails loginDetails){

    authenticationService.validatePermission(userId, loginDetails.getToken());

    return userService.getUserDtoById(userId);

  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(USER_URL)
  public UserDto register(@Validated @RequestBody UserDto userDto) {

    var responseDto = userService.createFromUserDto(userDto);

    var token = authenticationService.generateNewToken(userDto);

    responseDto.setToken(token);

    return responseDto;

  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(AUTH_URL)
  public LoginDetails authenticateAndUpdateToken(@Validated @RequestBody UserDto userDto) {

    return authenticationService.authenticateAndUpdateToken(userDto);

  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(USER_WITH_ID_URL)
  public UserDto update(@PathVariable Integer userId, @RequestBody UserDto userDto){

    authenticationService.validatePermission(userId, userDto.getToken());

    var responseDto = userService.updateByIdFromUserDto(userId, userDto);

    var newToken = authenticationService.generateNewToken(responseDto);

    responseDto.setToken(newToken);

    return responseDto;

  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(USER_WITH_ID_URL)
  public void delete(@PathVariable Integer userId, @RequestBody LoginDetails loginDetails){

    authenticationService.validatePermission(userId, loginDetails.getToken());

    userService.deleteById(userId);

  }


}
