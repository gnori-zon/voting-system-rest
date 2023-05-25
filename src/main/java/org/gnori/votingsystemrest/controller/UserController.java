package org.gnori.votingsystemrest.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1//users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

  UserService userService;

  public static final String USER_WITH_ID_URL = "/{userId}";



}
