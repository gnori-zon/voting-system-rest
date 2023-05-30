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

    var raw = new UserForAdminDto();
    raw.setId(5);
    raw.setUsername("un-1");
    raw.setPassword("pw-1");
    raw.setRoles(new HashSet<>(Set.of(Role.ADMIN)));

    var expectedValue = UserEntity.builder()
        .username(raw.getUsername())
        .password(raw.getPassword())
        .roles(raw.getRoles())
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

    var expectedValue = new UserForAdminDto();
    expectedValue.setId(raw.getId());
    expectedValue.setUsername(raw.getUsername());
    expectedValue.setRoles(raw.getRoles());
    expectedValue.setVotedFor(raw.getVotedFor());
    expectedValue.setDateVote(raw.getDateVote());

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

    var dto1 = new UserForAdminDto();
    dto1.setId(entity1.getId());
    dto1.setUsername(entity1.getUsername());
    dto1.setPassword(entity1.getPassword());
    dto1.setRoles(entity1.getRoles());
    dto1.setVotedFor(entity1.getVotedFor());
    dto1.setDateVote(entity1.getDateVote());

    var dto2 = new UserForAdminDto();
    dto2.setId(entity2.getId());
    dto2.setUsername(entity2.getUsername());
    dto2.setPassword(entity2.getPassword());
    dto2.setRoles(entity2.getRoles());
    dto2.setVotedFor(entity2.getVotedFor());
    dto2.setDateVote(entity2.getDateVote());

    var expectedValue = new ArrayList<>(List.of(dto1, dto2));

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
