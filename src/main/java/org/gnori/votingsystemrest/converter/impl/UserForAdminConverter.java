package org.gnori.votingsystemrest.converter.impl;

import java.util.List;
import org.gnori.votingsystemrest.converter.BaseConverter;
import org.gnori.votingsystemrest.converter.ListConverter;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserForAdminConverter implements BaseConverter<UserForAdminDto, UserEntity>,
    ListConverter<UserForAdminDto, UserEntity> {

  public UserForAdminDto convertFrom(UserEntity userEntity) {

    if (userEntity == null) return null;

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setId(userEntity.getId());
    userForAdminDto.setUsername(userEntity.getUsername());
    userForAdminDto.setRoles(userEntity.getRoles());
    userForAdminDto.setVotedFor(userEntity.getVotedFor());
    userForAdminDto.setDateVote(userEntity.getDateVote());
    return userForAdminDto;

  }

  public List<UserForAdminDto> convertListFrom(List<UserEntity> userEntityList) {

    return userEntityList.stream().map(this::convertFrom).toList();

  }

  public UserEntity convertFrom(UserForAdminDto userForAdminDto) {

    if (userForAdminDto == null) return null;

    return UserEntity.builder()
        .username(userForAdminDto.getUsername())
        .password(userForAdminDto.getPassword())
        .roles(userForAdminDto.getRoles())
        .build();

  }

}
