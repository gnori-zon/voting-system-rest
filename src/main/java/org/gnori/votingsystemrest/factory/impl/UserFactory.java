package org.gnori.votingsystemrest.factory.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.factory.BaseFactory;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory implements BaseFactory<UserDto, UserEntity> {

  public UserDto convertFrom(UserEntity userEntity) {

    if (userEntity == null) return null;

    return UserDto
        .builder()
        .id(userEntity.getId())
        .username(userEntity.getUsername())
        .roles(userEntity.getRoles())
        .build();
  }

  public List<UserDto> convertListFrom(List<UserEntity> userEntityList) {

    return userEntityList.stream().map(this::convertFrom).toList();

  }

  public UserEntity convertFrom(UserDto userDto) {

    if (userDto == null) return null;

    return UserEntity.builder()
        .username(userDto.getUsername())
        .password(userDto.getPassword())
        .build();

  }

}
