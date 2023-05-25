package org.gnori.votingsystemrest.service.impl;

import java.util.List;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.ConflictException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.UserFactory;
import org.gnori.votingsystemrest.factory.UserForAdminFactory;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<UserEntity, UserDao> {

  private final UserForAdminFactory userForAdminFactory;
  private final UserFactory userFactory;

  public UserService(UserDao dao, UserForAdminFactory userForAdminFactory, UserFactory userFactory) {
    super(dao);
    this.userFactory = userFactory;
    this.userForAdminFactory = userForAdminFactory;
  }

  public UserForAdminDto getUserForAdminDtoById(Integer userId) {

    var userEntity = getAndValidById(userId);

    return userForAdminFactory.convertFrom(userEntity);

  }

  public UserDto getUserDtoById(Integer userId) {

    var userEntity = getAndValidById(userId);

    return userFactory.convertFrom(userEntity);
  }

  public List<UserForAdminDto> getAllUserForAdminDto() {

    return userForAdminFactory.convertListFrom(getAll());

  }

  public List<UserDto> getAllUserDto() {

    return userFactory.convertListFrom(getAll());

  }

  public UserForAdminDto createFromUserForAdminDto(UserForAdminDto userForAdminDto) {

    checkForExistUsername(userForAdminDto.getUsername());

    var userEntity = userForAdminFactory.convertFrom(userForAdminDto);

    return userForAdminFactory.convertFrom(create(userEntity));

  }

  public UserDto createFromUserDto(UserDto userDto) {

    checkForExistUsername(userDto.getUsername());

    var userEntity = userFactory.convertFrom(userDto);

    return userFactory.convertFrom(create(userEntity));
  }

  public UserForAdminDto updateFromUserForAdminDtoById(Integer userId,
      UserForAdminDto userForAdminDto) {

    var user = getAndValidById(userId);

    if (!user.getUsername().equals(userForAdminDto.getUsername())) {

      checkForExistUsername(userForAdminDto.getUsername());

    }

    var newUser = userForAdminFactory.convertFrom(userForAdminDto);

    return userForAdminFactory.convertFrom(
        update(userId, newUser).orElse(null)
    );

  }

  private UserEntity getAndValidById(Integer userId) {

    return get(userId).orElseThrow(
        () -> new NotFoundException(
            String.format("user with id: %d is not exist", userId),
            HttpStatus.NOT_FOUND
        )
    );

  }


  private void checkForExistUsername(String username) {

    if(dao.existsByUsername(username)) {
      throw new ConflictException(
          String.format("user with name: %s already is exist", username),
          HttpStatus.CONFLICT
      );
    }

  }
}
