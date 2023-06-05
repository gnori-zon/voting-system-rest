package org.gnori.votingsystemrest.converter.impl;

import java.util.List;
import org.gnori.votingsystemrest.converter.BaseConverter;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements BaseConverter<UserDto, UserEntity> {

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
