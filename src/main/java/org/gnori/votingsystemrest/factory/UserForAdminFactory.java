package org.gnori.votingsystemrest.factory;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserForAdminFactory implements BaseFactory<UserForAdminDto, UserEntity> {

  private final UserFactory userFactory;

  public UserForAdminDto convertFrom(UserEntity userEntity) {

    var userForAdminDto = new UserForAdminDto();
    userForAdminDto.setId(userEntity.getId());
    userForAdminDto.setUsername(userEntity.getUsername());
    userForAdminDto.setPassword(userEntity.getPassword());
    userForAdminDto.setRoles(userEntity.getRoles());
    userForAdminDto.setVotedFor(userEntity.getVotedFor());
    userForAdminDto.setDateVote(userEntity.getDateVote());
    return userForAdminDto;

  }

  public List<UserForAdminDto> convertListFrom(List<UserEntity> userEntityList) {

    return userEntityList.stream().map(this::convertFrom).toList();

  }

  public UserEntity convertFrom(UserForAdminDto userForAdminDto) {

    var userEntity = userFactory.convertFrom(userForAdminDto);
    userEntity.setRoles(userForAdminDto.getRoles());

    return userEntity;

  }

}
