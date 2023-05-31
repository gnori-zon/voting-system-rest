package org.gnori.votingsystemrest.unit.factory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gnori.votingsystemrest.factory.impl.UserForAdminFactory;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit test for UserForAdminFactory")
class UserForAdminFactoryTest extends AbstractFactoryTest<UserForAdminDto, UserEntity> {

  UserForAdminFactoryTest() {
    super(new UserForAdminFactory());
  }

  @Test
  void convertFromTest1() {

    var rawUserForAdminDto = new UserForAdminDto();
    rawUserForAdminDto.setId(5);
    rawUserForAdminDto.setUsername("un-1");
    rawUserForAdminDto.setPassword("pw-1");
    rawUserForAdminDto.setRoles(new HashSet<>(Set.of(Role.ADMIN)));

    var expectedValue = UserEntity.builder()
        .username(rawUserForAdminDto.getUsername())
        .password(rawUserForAdminDto.getPassword())
        .roles(rawUserForAdminDto.getRoles())
        .build();

    convertFromDtoTest(rawUserForAdminDto, expectedValue);

  }

  @Test
  void convertFromTest2() {

    var rawUserEntity = UserEntity.builder()
        .username("un-1")
        .password("pw-1")
        .votedFor(1)
        .dateVote(LocalDate.now())
        .roles(new HashSet<>(Set.of(Role.ADMIN)))
        .build();
    rawUserEntity.setId(5);

    var expectedValue = new UserForAdminDto();
    expectedValue.setId(rawUserEntity.getId());
    expectedValue.setUsername(rawUserEntity.getUsername());
    expectedValue.setRoles(rawUserEntity.getRoles());
    expectedValue.setVotedFor(rawUserEntity.getVotedFor());
    expectedValue.setDateVote(rawUserEntity.getDateVote());

    convertFromEntityTest(rawUserEntity, expectedValue);

  }

  @Test
  void convertAllFromTest() {

    var userEntity1 = UserEntity.builder()
        .username("un-1")
        .password("pw-1")
        .votedFor(1)
        .dateVote(LocalDate.now())
        .roles(new HashSet<>(Set.of(Role.ADMIN)))
        .build();
    userEntity1.setId(5);

    var userEntity2 = UserEntity.builder()
        .username("un-2")
        .password("pw-2")
        .votedFor(2)
        .dateVote(LocalDate.now())
        .roles(new HashSet<>(Set.of(Role.USER)))
        .build();
    userEntity2.setId(6);

    var raw = new ArrayList<>(List.of(userEntity1, userEntity2));

    var userForAdminDto1 = new UserForAdminDto();
    userForAdminDto1.setId(userEntity1.getId());
    userForAdminDto1.setUsername(userEntity1.getUsername());
    userForAdminDto1.setPassword(userEntity1.getPassword());
    userForAdminDto1.setRoles(userEntity1.getRoles());
    userForAdminDto1.setVotedFor(userEntity1.getVotedFor());
    userForAdminDto1.setDateVote(userEntity1.getDateVote());

    var userForAdminDto2 = new UserForAdminDto();
    userForAdminDto2.setId(userEntity2.getId());
    userForAdminDto2.setUsername(userEntity2.getUsername());
    userForAdminDto2.setPassword(userEntity2.getPassword());
    userForAdminDto2.setRoles(userEntity2.getRoles());
    userForAdminDto2.setVotedFor(userEntity2.getVotedFor());
    userForAdminDto2.setDateVote(userEntity2.getDateVote());

    var expectedValue = new ArrayList<>(List.of(userForAdminDto1, userForAdminDto2));

    convertFromListEntityTest(raw, expectedValue);

  }

  @Override
  void convertFromDtoTest(UserForAdminDto raw, UserEntity expectedResult) {

    var actual = factory.convertFrom(raw);

    Assertions.assertEquals(expectedResult.getUsername(), actual.getUsername());
    Assertions.assertEquals(expectedResult.getPassword(), actual.getPassword());
    Assertions.assertEquals(expectedResult.getRoles(), actual.getRoles());

    Assertions.assertNull(actual.getId());
    Assertions.assertNull(actual.getVotedFor());
    Assertions.assertNull(actual.getDateVote());

  }
}
