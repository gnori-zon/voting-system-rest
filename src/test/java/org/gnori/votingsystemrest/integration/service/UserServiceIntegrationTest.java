package org.gnori.votingsystemrest.integration.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.ConflictException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.UserFactory;
import org.gnori.votingsystemrest.factory.UserForAdminFactory;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.gnori.votingsystemrest.service.impl.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DataJpaTest
@DisplayName("Integration test for UserService and JPA")
class UserServiceIntegrationTest {

  private final UserService service;

  private UserDto rawDto;

  private UserEntity rawEntity;

  public UserServiceIntegrationTest(@Autowired UserDao userDao) {
    this.service = new UserService(
        userDao, new UserForAdminFactory(), new BCryptPasswordEncoder(),  new UserFactory()
    );
  }

  @BeforeEach
  void updateRaw() {
    rawDto = UserDto.builder()
        .id(5)
        .username("user")
        .password("password")
        .roles(Set.of(Role.ADMIN))
        .build();

    rawEntity = UserEntity.builder()
        .username("admin")
        .password("password")
        .roles(new HashSet<>(Set.of(Role.USER)))
        .dateVote(LocalDate.now())
        .votedFor(1)
        .build();

  }
  // GET
  @Test
  void getAllForUserSuccess() {

    rawEntity = service.create(rawEntity);

    var actualAll = service.getAllUserDto();
    var actual = actualAll.stream().findFirst().orElse(null);

    Assertions.assertNotNull(actualAll);
    Assertions.assertFalse(actualAll.isEmpty());
    Assertions.assertEquals(1, actualAll.size());

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(rawEntity.getId(), actual.getId());
    Assertions.assertEquals(rawEntity.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawEntity.getPassword(), actual.getPassword());
    Assertions.assertEquals(rawEntity.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void getAllForAdminSuccess() {

    rawEntity = service.create(rawEntity);

    var actualAll = service.getAllUserForAdminDto();
    var actual = actualAll.stream().findFirst().orElse(null);

    Assertions.assertNotNull(actualAll);
    Assertions.assertFalse(actualAll.isEmpty());
    Assertions.assertEquals(1, actualAll.size());

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(rawEntity.getId(), actual.getId());
    Assertions.assertEquals(rawEntity.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawEntity.getPassword(), actual.getPassword());
    Assertions.assertEquals(rawEntity.getVotedFor(), actual.getVotedFor());
    Assertions.assertEquals(rawEntity.getDateVote(), actual.getDateVote());
    Assertions.assertEquals(rawEntity.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }
//
//  @Test
//  void getByIdForUserSuccess() {
//
//    rawEntity = service.create(rawEntity);
//
//    var actual = service.getUserDtoById(rawEntity.getId());
//
//    Assertions.assertNotNull(actual);
//
//    Assertions.assertNotNull(actual);
//    Assertions.assertEquals(rawEntity.getId(), actual.getId());
//    Assertions.assertEquals(rawEntity.getUsername(), actual.getUsername());
//    Assertions.assertEquals(rawEntity.getPassword(), actual.getPassword());
//    Assertions.assertEquals(rawEntity.getRoles(), actual.getRoles());
//
//    service.delete(actual.getId());
//
//  }

//  @Test
//  void getByIdForUserShouldThrowNotFoundException() {
//
//    Assertions.assertThrows(
//        NotFoundException.class, () -> service.getUserDtoById(1_000)
//    );
//
//  }

  @Test
  void getByIdForAdminSuccess() {

    rawEntity = service.create(rawEntity);

    var actual = service.getUserForAdminDtoById(rawEntity.getId());

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawEntity.getId(), actual.getId());
    Assertions.assertEquals(rawEntity.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawEntity.getPassword(), actual.getPassword());
    Assertions.assertEquals(rawEntity.getVotedFor(), actual.getVotedFor());
    Assertions.assertEquals(rawEntity.getDateVote(), actual.getDateVote());
    Assertions.assertEquals(rawEntity.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void getByIdForAdminShouldThrowNotFoundException() {

    Assertions.assertThrows(
        NotFoundException.class, () -> service.getUserForAdminDtoById(1_000)
    );

  }

  // CREATE
  @Test
  void createFromUserDtoSuccess() {

    var actual = service.createFromUserDto(rawDto);

    Assertions.assertNotNull(actual);

    Assertions.assertNotNull(actual);
    Assertions.assertNotEquals(rawDto.getId(), actual.getId());
    Assertions.assertEquals(rawDto.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawDto.getPassword(), actual.getPassword());
    Assertions.assertEquals(new HashSet<>(Set.of(Role.USER)), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void createFromUserDtoShouldThrowConflictException() {

    rawEntity = service.create(rawEntity);

    rawDto.setUsername(rawEntity.getUsername());

    Assertions.assertThrows(
        ConflictException.class, () -> service.createFromUserDto(rawDto)
    );

    service.delete(rawEntity.getId());

  }

  @Test
  void createFromUserForAdminDtoSuccess() {

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setUsername(rawEntity.getUsername());
    userForAdminDto.setPassword(rawEntity.getUsername());
    userForAdminDto.setRoles(new HashSet<>(Set.of(Role.ADMIN)));

    var actual = service.createFromUserForAdminDto(userForAdminDto);

    Assertions.assertNotNull(actual);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(userForAdminDto.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(userForAdminDto.getPassword(), actual.getPassword());
    Assertions.assertEquals(userForAdminDto.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void createFromUserForAdminDtoShouldThrowConflictException() {

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setUsername(rawEntity.getUsername());
    userForAdminDto.setPassword("Password");
    userForAdminDto.setRoles(Set.of(Role.ADMIN));

    rawEntity = service.create(rawEntity);

    Assertions.assertThrows(
        ConflictException.class, () -> service.createFromUserForAdminDto(userForAdminDto)
    );

    service.delete(rawEntity.getId());

  }

  // UPDATE
  @Test
  void updateFromUserForAdminDtoByIdSuccess() {

    rawEntity = service.create(rawEntity);

    var oldUsername = rawEntity.getUsername();
    var oldPassword = rawEntity.getPassword();
    var oldRoles = new HashSet<>(rawEntity.getRoles());

    var newUsername = "newName";
    var newPassword = "newPassword";
    var newRoles = new HashSet<>(Set.of(Role.ADMIN));

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setUsername(newUsername);
    userForAdminDto.setPassword(newPassword);
    userForAdminDto.setRoles(newRoles);


    var actual = service.updateFromUserForAdminDtoById(rawEntity.getId(), userForAdminDto);

    Assertions.assertNotNull(actual);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(rawEntity.getId(), actual.getId());
    Assertions.assertEquals(userForAdminDto.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(oldUsername, actual.getUsername());
    Assertions.assertNotEquals(userForAdminDto.getPassword(), actual.getPassword());
    Assertions.assertNotEquals(oldPassword, actual.getPassword());
    Assertions.assertEquals(userForAdminDto.getRoles(), actual.getRoles());
    Assertions.assertNotEquals(oldRoles, actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void updateFromUserForAdminDtoByIdShouldThrowConflictException() {

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setUsername(rawEntity.getUsername());
    userForAdminDto.setPassword("Password");
    userForAdminDto.setRoles(new HashSet<>(Set.of(Role.ADMIN)));

    var firstId = service.create(rawEntity).getId();

    var secondEntity = new UserEntity(
        "newName", rawEntity.getPassword(), new HashSet<>(Set.of(Role.ADMIN)),
        rawEntity.getVotedFor(), rawEntity.getDateVote());

    secondEntity = service.create(secondEntity);

    var finalSecondEntity = secondEntity;
    Assertions.assertThrows(
        ConflictException.class,
        () -> service.updateFromUserForAdminDtoById(finalSecondEntity.getId(), userForAdminDto)
    );

    service.delete(rawEntity.getId());
    service.delete(firstId);

  }

  @Test
  void updateFromUserForAdminDtoByIdShouldThrowNotFoundException() {

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setUsername("UserName");
    userForAdminDto.setPassword("Password");

    Assertions.assertThrows(
        NotFoundException.class,
        () -> service.updateFromUserForAdminDtoById(1_000, userForAdminDto)
    );

  }





































}
