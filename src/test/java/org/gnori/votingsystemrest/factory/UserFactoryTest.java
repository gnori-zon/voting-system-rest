package org.gnori.votingsystemrest.factory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit test for UserFactory")
class UserFactoryTest extends AbstractFactoryTest<UserDto, UserEntity> {

  UserFactoryTest() {
    super(new UserFactory());
  }

  @Test
  void convertFromTest1() {

    var raw = UserDto.builder()
        .id(5)
        .username("un-1")
        .password("pw-1")
        .roles(new HashSet<>(Set.of(Role.ADMIN)))
        .build();

    var expectedValue = UserEntity.builder()
        .username(raw.getUsername())
        .password(raw.getPassword())
        .build();

    convertFromDtoTest(raw, expectedValue);

  }

  @Test
  void convertFromTest2() {

    var raw = UserEntity.builder()
        .username("un-1")
        .password("pw-1")
        .votedFor(1)
        .dateVote(LocalDate.now())
        .roles(new HashSet<>(Set.of(Role.ADMIN)))
        .build();
    raw.setId(5);

    var expectedValue = UserDto.builder()
        .id(raw.getId())
        .username(raw.getUsername())
        .password(raw.getPassword())
        .roles(raw.getRoles())
        .build();

    convertFromEntityTest(raw, expectedValue);

  }

  @Test
  void convertAllFromTest() {

    var entity1 = UserEntity.builder()
        .username("un-1")
        .password("pw-1")
        .votedFor(1)
        .dateVote(LocalDate.now())
        .roles(new HashSet<>(Set.of(Role.ADMIN)))
        .build();
    entity1.setId(5);

    var entity2 = UserEntity.builder()
        .username("un-2")
        .password("pw-2")
        .votedFor(2)
        .dateVote(LocalDate.now())
        .roles(new HashSet<>(Set.of(Role.USER)))
        .build();
    entity1.setId(6);

    var raw = new ArrayList<>(List.of(entity1, entity2));

    var dto1 = UserDto.builder()
        .id(entity1.getId())
        .username(entity1.getUsername())
        .password(entity1.getPassword())
        .roles(entity1.getRoles())
        .build();

    var dto2 =UserDto.builder()
        .id(entity2.getId())
        .username(entity2.getUsername())
        .password(entity2.getPassword())
        .roles(entity2.getRoles())
        .build();

    var expectedValue = new ArrayList<>(List.of(dto1, dto2));

    convertFromListEntityTest(raw, expectedValue);

  }

  @Override
  void convertFromDtoTest(UserDto raw, UserEntity expectedResult) {

    var actual = factory.convertFrom(raw);

    Assertions.assertEquals(expectedResult.getUsername(), actual.getUsername());
    Assertions.assertEquals(expectedResult.getPassword(), actual.getPassword());

    Assertions.assertNull(actual.getId());
    Assertions.assertNull(actual.getVotedFor());
    Assertions.assertNull(actual.getDateVote());
    Assertions.assertNull(actual.getRoles());

  }
}
