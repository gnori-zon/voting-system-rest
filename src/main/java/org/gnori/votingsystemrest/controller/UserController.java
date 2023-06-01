package org.gnori.votingsystemrest.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.log.annotation.LogMethodExecutionTime;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.dto.UserDto.AdvancedValidation;
import org.gnori.votingsystemrest.model.dto.UserDto.ValidationOfUser;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.gnori.votingsystemrest.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

  private static final String AUTH_HEADER = "Authorization";

  UserService userService;
  AuthenticationService<UserDto, Integer, String> authenticationService;

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = USER_WITH_ID_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDto get(@PathVariable Integer userId, @RequestHeader(AUTH_HEADER) String token){

    authenticationService.validatePermission(userId, token);

    return userService.getUserDtoById(userId);

  }

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = USER_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDto register(@Validated(ValidationOfUser.class) @RequestBody UserDto userDto) {

    var responseUserDto = userService.createFromUserDto(userDto);

    var token = authenticationService.generateNewToken(userDto);

    responseUserDto.setToken(token);

    return responseUserDto;

  }

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = AUTH_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDto authenticateAndUpdateToken(
      @Validated(ValidationOfUser.class) @RequestBody UserDto userDto) {

    authenticationService.authenticate(userDto);

    var token = authenticationService.generateNewToken(userDto);

    return UserDto.builder()
        .token(token)
        .build();

  }

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping(value = USER_WITH_ID_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDto update(
      @PathVariable Integer userId,
      @RequestHeader(AUTH_HEADER) String token,
      @Validated(AdvancedValidation.class) @RequestBody UserDto userDto){

    authenticationService.validatePermission(userId, token);

    var responseUserDto = userService.updateByIdFromUserDto(userId, userDto);

    var newToken = authenticationService.generateNewToken(responseUserDto);

    responseUserDto.setToken(newToken);

    return responseUserDto;

  }

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(USER_WITH_ID_URL)
  public void delete(@PathVariable Integer userId, @RequestHeader(AUTH_HEADER) String token){

    authenticationService.validatePermission(userId, token);

    userService.delete(userId);

  }


}
