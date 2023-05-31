package org.gnori.votingsystemrest.controller;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {

  public static final String ADMIN_USERS_WITH_ID_URL = "/{userId}";

  UserService userService;
  AuthenticationService<UserDto, Integer, String> authenticationService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = ADMIN_USERS_WITH_ID_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserForAdminDto get(@PathVariable Integer userId) {

    return userService.getUserForAdminDtoById(userId);

  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<UserForAdminDto> getAll() {

    return userService.getAllUserForAdminDto();

  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserForAdminDto create(@Validated(ValidationOfAdmin.class) @RequestBody UserForAdminDto userForAdminDto) {

    var responseDto =  userService.createFromUserForAdminDto(userForAdminDto);

    var token = authenticationService.generateNewToken(userForAdminDto);

    responseDto.setToken(token);

    return responseDto;

  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping(value = ADMIN_USERS_WITH_ID_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserForAdminDto update(@PathVariable Integer userId, @Validated(AdvancedValidation.class) @RequestBody UserForAdminDto userForAdminDto) {

    var responseDto = userService.updateFromUserForAdminDtoById(userId, userForAdminDto);

    var token = authenticationService.generateNewToken(userForAdminDto);

    responseDto.setToken(token);

    return responseDto;

  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(ADMIN_USERS_WITH_ID_URL)
  public void delete(@PathVariable Integer userId) {

    userService.delete(userId);

  }

}
