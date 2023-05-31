package org.gnori.votingsystemrest.integration.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.exceptions.impl.ConflictException;
import org.gnori.votingsystemrest.error.exceptions.impl.NotFoundException;
import org.gnori.votingsystemrest.factory.impl.UserFactory;
import org.gnori.votingsystemrest.factory.impl.UserForAdminFactory;
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

  private UserDto rawUserDto;

  private UserEntity rawUserEntity;

  public UserServiceIntegrationTest(@Autowired UserDao userDao) {
    this.service = new UserService(
        userDao, new UserForAdminFactory(), new BCryptPasswordEncoder(),  new UserFactory()
    );
  }

  @BeforeEach
  void updateRaw() {
    rawUserDto = UserDto.builder()
        .id(5)
        .username("user")
        .password("password")
        .roles(Set.of(Role.ADMIN))
        .build();

    rawUserEntity = UserEntity.builder()
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

    rawUserEntity = service.create(rawUserEntity);

    var actualAll = service.getAllUserDto();
    var actual = actualAll.stream().findFirst().orElse(null);

    Assertions.assertNotNull(actualAll);
    Assertions.assertFalse(actualAll.isEmpty());
    Assertions.assertEquals(1, actualAll.size());

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(rawUserEntity.getId(), actual.getId());
    Assertions.assertEquals(rawUserEntity.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawUserEntity.getPassword(), actual.getPassword());
    Assertions.assertEquals(rawUserEntity.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void getAllForAdminSuccess() {

    rawUserEntity = service.create(rawUserEntity);

    var actualAll = service.getAllUserForAdminDto();
    var actual = actualAll.stream().findFirst().orElse(null);

    Assertions.assertNotNull(actualAll);
    Assertions.assertFalse(actualAll.isEmpty());
    Assertions.assertEquals(1, actualAll.size());

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(rawUserEntity.getId(), actual.getId());
    Assertions.assertEquals(rawUserEntity.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawUserEntity.getPassword(), actual.getPassword());
    Assertions.assertEquals(rawUserEntity.getVotedFor(), actual.getVotedFor());
    Assertions.assertEquals(rawUserEntity.getDateVote(), actual.getDateVote());
    Assertions.assertEquals(rawUserEntity.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void getByIdAndUsernameForUserSuccess() {

    rawUserEntity = service.create(rawUserEntity);

    var actual = service.getUserDtoById(rawUserEntity.getId());

    Assertions.assertNotNull(actual);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(rawUserEntity.getId(), actual.getId());
    Assertions.assertEquals(rawUserEntity.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawUserEntity.getPassword(), actual.getPassword());
    Assertions.assertEquals(rawUserEntity.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void getByIdAndUsernameForUserShouldThrowNotFoundException() {

    Assertions.assertThrows(
        NotFoundException.class, () -> service.getUserDtoById(1_000)
    );

  }

  @Test
  void getByIdForAdminSuccess() {

    rawUserEntity = service.create(rawUserEntity);

    var actual = service.getUserForAdminDtoById(rawUserEntity.getId());

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawUserEntity.getId(), actual.getId());
    Assertions.assertEquals(rawUserEntity.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawUserEntity.getPassword(), actual.getPassword());
    Assertions.assertEquals(rawUserEntity.getVotedFor(), actual.getVotedFor());
    Assertions.assertEquals(rawUserEntity.getDateVote(), actual.getDateVote());
    Assertions.assertEquals(rawUserEntity.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void getByIdForAdminShouldThrowNotFoundException() {

    Assertions.assertThrows(
        NotFoundException.class, () -> service.getUserForAdminDtoById(1_000)
    );

  }

  @Test
  void getByUsernameSuccess() {

    rawUserEntity = service.create(rawUserEntity);

    var actual = service.getByUsername(rawUserEntity.getUsername());

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawUserEntity.getId(), actual.getId());
    Assertions.assertEquals(rawUserEntity.getUsername(), actual.getUsername());
    Assertions.assertEquals(rawUserEntity.getVotedFor(), actual.getVotedFor());
    Assertions.assertEquals(rawUserEntity.getDateVote(), actual.getDateVote());
    Assertions.assertEquals(rawUserEntity.getRoles(), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void getByUsernameShouldThrowNotFoundException() {

    Assertions.assertThrows(
        NotFoundException.class, () -> service.getByUsername("username")
    );

  }
  // CREATE
  @Test
  void createFromUserDtoSuccess() {

    var actual = service.createFromUserDto(rawUserDto);

    Assertions.assertNotNull(actual);

    Assertions.assertNotNull(actual);
    Assertions.assertNotEquals(rawUserDto.getId(), actual.getId());
    Assertions.assertEquals(rawUserDto.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(rawUserDto.getPassword(), actual.getPassword());
    Assertions.assertEquals(new HashSet<>(Set.of(Role.USER)), actual.getRoles());

    service.delete(actual.getId());

  }

  @Test
  void createFromUserDtoShouldThrowConflictException() {

    rawUserEntity = service.create(rawUserEntity);

    rawUserDto.setUsername(rawUserEntity.getUsername());

    Assertions.assertThrows(
        ConflictException.class, () -> service.createFromUserDto(rawUserDto)
    );

    service.delete(rawUserEntity.getId());

  }

  @Test
  void createFromUserForAdminDtoSuccess() {

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setUsername(rawUserEntity.getUsername());
    userForAdminDto.setPassword(rawUserEntity.getUsername());
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
    userForAdminDto.setUsername(rawUserEntity.getUsername());
    userForAdminDto.setPassword("Password");
    userForAdminDto.setRoles(Set.of(Role.ADMIN));

    rawUserEntity = service.create(rawUserEntity);

    Assertions.assertThrows(
        ConflictException.class, () -> service.createFromUserForAdminDto(userForAdminDto)
    );

    service.delete(rawUserEntity.getId());

  }

  // UPDATE
  @Test
  void updateFromUserForAdminDtoByIdSuccess() {

    rawUserEntity = service.create(rawUserEntity);

    var oldUsername = rawUserEntity.getUsername();
    var oldPassword = rawUserEntity.getPassword();
    var oldRoles = new HashSet<>(rawUserEntity.getRoles());

    var newUsername = "newName";
    var newPassword = "newPassword";
    var newRoles = new HashSet<>(Set.of(Role.ADMIN));

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setUsername(newUsername);
    userForAdminDto.setPassword(newPassword);
    userForAdminDto.setRoles(newRoles);


    var actual = service.updateFromUserForAdminDtoById(rawUserEntity.getId(), userForAdminDto);

    Assertions.assertNotNull(actual);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(rawUserEntity.getId(), actual.getId());
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
    userForAdminDto.setUsername(rawUserEntity.getUsername());
    userForAdminDto.setPassword("Password");
    userForAdminDto.setRoles(new HashSet<>(Set.of(Role.ADMIN)));

    var firstId = service.create(rawUserEntity).getId();

    var secondEntity = new UserEntity(
        "newName", rawUserEntity.getPassword(), new HashSet<>(Set.of(Role.ADMIN)),
        rawUserEntity.getVotedFor(), rawUserEntity.getDateVote());

    secondEntity = service.create(secondEntity);

    var finalSecondEntity = secondEntity;
    Assertions.assertThrows(
        ConflictException.class,
        () -> service.updateFromUserForAdminDtoById(finalSecondEntity.getId(), userForAdminDto)
    );

    service.delete(rawUserEntity.getId());
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

  @Test
  void updateFromUserForUserDtoSuccess() {

    rawUserEntity = service.create(rawUserEntity);

    var oldUsername = rawUserEntity.getUsername();
    var oldPassword = rawUserEntity.getPassword();

    var newUsername = "newName";
    var newPassword = "newPassword";

    var userDto = UserDto.builder()
        .username(newUsername)
        .password(newPassword)
        .build();

    var actual = service.updateByIdFromUserDto(rawUserEntity.getId(), userDto);

    Assertions.assertNotNull(actual);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(rawUserEntity.getId(), actual.getId());
    Assertions.assertEquals(userDto.getUsername(), actual.getUsername());
    Assertions.assertNotEquals(oldUsername, actual.getUsername());
    Assertions.assertNotEquals(userDto.getPassword(), actual.getPassword());
    Assertions.assertNotEquals(oldPassword, actual.getPassword());

    service.delete(actual.getId());

  }

  @Test
  void updateFromUserForUserDtoShouldThrowNotFoundException() {

    Assertions.assertThrows(NotFoundException.class,
        () -> service.updateByIdFromUserDto(1_000, new UserDto()));

  }

  @Test
  void updateFromUserForUserDtoShouldThrowConflictException() {

    var firstEntity = service.create(
        UserEntity.builder()
            .username("userI")
            .password("pass1")
            .build()
    );
    rawUserEntity = service.create(rawUserEntity);

    var newUserDto = UserDto.builder()
        .username(firstEntity.getUsername())
        .password(firstEntity.getPassword())
        .build();

    Assertions.assertThrows(ConflictException.class,
        () -> service.updateByIdFromUserDto(rawUserEntity.getId(), newUserDto));


    service.delete(rawUserEntity.getId());
    service.delete(firstEntity.getId());
  }

}
