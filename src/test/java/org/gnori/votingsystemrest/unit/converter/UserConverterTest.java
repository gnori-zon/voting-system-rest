package org.gnori.votingsystemrest.unit.converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gnori.votingsystemrest.converter.impl.UserConverter;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit test for UserFactory")
class UserConverterTest extends AbstractConverterTest<UserDto, UserEntity> {

  UserConverterTest() {
    super(new UserConverter(), new UserConverter());
  }

  @Test
  void convertFromTest1() {

    var rawUserDto = UserDto.builder()
        .id(5)
        .username("un-1")
        .password("pw-1")
        .roles(new HashSet<>(Set.of(Role.ADMIN)))
        .build();

    var expectedValue = UserEntity.builder()
        .username(rawUserDto.getUsername())
        .password(rawUserDto.getPassword())
        .build();

    convertFromDtoTest(rawUserDto, expectedValue);

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

    var expectedValue = UserDto.builder()
        .id(rawUserEntity.getId())
        .username(rawUserEntity.getUsername())
        .roles(rawUserEntity.getRoles())
        .build();

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

    var userDto1 = UserDto.builder()
        .id(userEntity1.getId())
        .username(userEntity1.getUsername())
        .roles(userEntity1.getRoles())
        .build();

    var userDto2 =UserDto.builder()
        .id(userEntity2.getId())
        .username(userEntity2.getUsername())
        .roles(userEntity2.getRoles())
        .build();

    var expectedValue = new ArrayList<>(List.of(userDto1, userDto2));

    convertFromListEntityTest(raw, expectedValue);

  }

  @Override
  void convertFromDtoTest(UserDto raw, UserEntity expectedResult) {

    var actual = converter.convertFrom(raw);

    Assertions.assertEquals(expectedResult.getUsername(), actual.getUsername());
    Assertions.assertEquals(expectedResult.getPassword(), actual.getPassword());

    Assertions.assertNull(actual.getId());
    Assertions.assertNull(actual.getVotedFor());
    Assertions.assertNull(actual.getDateVote());
    Assertions.assertNull(actual.getRoles());

  }
}
