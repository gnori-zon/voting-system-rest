package org.gnori.votingsystemrest.controller;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/api/v1/admin/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {

  UserService userService;

  public static final String ADMIN_USERS_WITH_ID_URL = "/{userId}";

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
  public UserForAdminDto create(@RequestBody UserForAdminDto userForAdminDto) {

    return userService.createFromUserForAdminDto(userForAdminDto);

  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = ADMIN_USERS_WITH_ID_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UserForAdminDto update(@PathVariable Integer userId, @RequestBody UserForAdminDto userForAdminDto) {

    return userService.updateFromUserForAdminDtoById(userId, userForAdminDto);

  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(ADMIN_USERS_WITH_ID_URL)
  public void delete (@PathVariable Integer userId) {

    userService.delete(userId);

  }

}
