package org.gnori.votingsystemrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.controller.constant.Endpoint;
import org.gnori.votingsystemrest.log.annotation.LogMethodExecutionTime;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.dto.UserDto.AdvancedValidation;
import org.gnori.votingsystemrest.model.dto.UserDto.ValidationOfAdmin;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "user controller for users with ADMIN role")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

  UserService userService;
  AuthenticationService<UserDto, Integer, String> authenticationService;

  @Operation(description = "get user for admin")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = Endpoint.ADMIN_USERS_WITH_ID_URL,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public UserForAdminDto get(@PathVariable Integer userId) {

    return userService.getUserForAdminDtoById(userId);

  }

  @Operation(description = "get all users for admin")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = Endpoint.ADMIN_USERS, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<UserForAdminDto> getAll() {

    return userService.getAllUserForAdminDto();

  }

  @Operation(description = "create user for admin")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = Endpoint.ADMIN_USERS,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public UserForAdminDto create(
      @Validated(ValidationOfAdmin.class) @RequestBody UserForAdminDto userForAdminDto) {

    var responseUserForAdminDto =  userService.createFromUserForAdminDto(userForAdminDto);

    var token = authenticationService.generateNewToken(userForAdminDto);

    responseUserForAdminDto.setToken(token);

    return responseUserForAdminDto;

  }

  @Operation(description = "update user data for admin")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping(value = Endpoint.ADMIN_USERS_WITH_ID_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserForAdminDto update(
      @PathVariable Integer userId,
      @Validated(AdvancedValidation.class) @RequestBody UserForAdminDto userForAdminDto) {

    var responseUserForAdminDto = userService.updateFromUserForAdminDtoById(userId, userForAdminDto);

    var token = authenticationService.generateNewToken(userForAdminDto);

    responseUserForAdminDto.setToken(token);

    return responseUserForAdminDto;

  }

  @Operation(description = "delete user for admin")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(Endpoint.ADMIN_USERS_WITH_ID_URL)
  public void delete(@PathVariable Integer userId) {

    userService.delete(userId);

  }

}
