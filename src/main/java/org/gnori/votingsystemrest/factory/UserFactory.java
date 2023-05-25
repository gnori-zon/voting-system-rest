package org.gnori.votingsystemrest.factory;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

  public UserDto convertFrom(UserEntity userEntity) {

    return UserDto
        .builder()
        .id(userEntity.getId())
        .username(userEntity.getUsername())
        .password(userEntity.getPassword())
        .roles(userEntity.getRoles())
        .build();
  }

  public List<UserDto> convertListFrom(List<UserEntity> userEntityList) {

    return userEntityList.stream().map(this::convertFrom).toList();

  }

  public UserEntity convertFrom(UserDto userDto) {

    return UserEntity.builder()
        .username(userDto.getUsername())
        .password(userDto.getPassword())
        .build();

  }

}
