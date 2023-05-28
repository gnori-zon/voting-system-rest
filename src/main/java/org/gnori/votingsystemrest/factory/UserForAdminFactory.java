package org.gnori.votingsystemrest.factory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserForAdminFactory implements BaseFactory<UserForAdminDto, UserEntity> {

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

    var newRoles = (userForAdminDto.getRoles() != null && !userForAdminDto.getRoles().isEmpty())
        ? userForAdminDto.getRoles()
        : new HashSet<>(Set.of(Role.USER));

    return UserEntity.builder()
        .username(userForAdminDto.getUsername())
        .password(userForAdminDto.getPassword())
        .roles(newRoles)
        .build();

  }

}
