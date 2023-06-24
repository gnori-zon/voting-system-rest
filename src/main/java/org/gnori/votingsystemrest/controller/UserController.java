package org.gnori.votingsystemrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.controller.constant.Endpoint;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "user controller for users")
public class UserController {

  private static final String AUTH_HEADER = "Authorization";

  UserService userService;
  AuthenticationService<UserDto, Integer, String> authenticationService;

  @Operation(description = "get user for user")
  @SecurityRequirement(name = "bearerAuth")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = Endpoint.USER_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDto get(@RequestHeader(AUTH_HEADER) String token){

    var userId = authenticationService.getUserIdFrom(token);

    return userService.getUserDtoById(userId);

  }

  @Operation(description = "register new user")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = Endpoint.USER_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDto register(
      @Validated(ValidationOfUser.class) @RequestBody UserDto userDto) {

    var responseUserDto = userService.createFromUserDto(userDto);

    var token = authenticationService.generateNewToken(userDto);

    responseUserDto.setToken(token);

    return responseUserDto;

  }

  @Operation(description = "auth user and update token")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = Endpoint.AUTH_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDto authenticateAndUpdateToken(
      @Validated(ValidationOfUser.class) @RequestBody UserDto userDto) {

    authenticationService.authenticate(userDto);

    var token = authenticationService.generateNewToken(userDto);

    return UserDto.builder()
        .token(token)
        .build();

  }

  @Operation(description = "update user data for user")
  @SecurityRequirement(name = "bearerAuth")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping(value = Endpoint.USER_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDto update(
      @RequestHeader(AUTH_HEADER) String token,
      @Validated(AdvancedValidation.class) @RequestBody UserDto userDto){

    var userId = authenticationService.getUserIdFrom(token);

    var responseUserDto = userService.updateByIdFromUserDto(userId, userDto);

    var newToken = authenticationService.generateNewToken(responseUserDto);

    responseUserDto.setToken(newToken);

    return responseUserDto;

  }

  @Operation(description = "delete user")
  @SecurityRequirement(name = "bearerAuth")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(Endpoint.USER_URL)
  public void delete(@RequestHeader(AUTH_HEADER) String token){

    var userId = authenticationService.getUserIdFrom(token);

    userService.delete(userId);

  }


}
