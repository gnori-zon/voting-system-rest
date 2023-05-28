package org.gnori.votingsystemrest.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.ConflictException;
import org.gnori.votingsystemrest.error.ForbiddenException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.UserFactory;
import org.gnori.votingsystemrest.factory.UserForAdminFactory;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<UserEntity, UserDao> {

  private final UserForAdminFactory userForAdminFactory;
  private final UserFactory userFactory;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserDao dao,
      UserForAdminFactory userForAdminFactory,
      PasswordEncoder passwordEncoder,
      UserFactory userFactory) {
    super(dao);
    this.userFactory = userFactory;
    this.passwordEncoder = passwordEncoder;
    this.userForAdminFactory = userForAdminFactory;
  }

  public UserForAdminDto getUserForAdminDtoById(Integer userId) {

    var userEntity = getAndValidateById(userId);

    return userForAdminFactory.convertFrom(userEntity);

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

    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

    return userForAdminFactory.convertFrom(create(userEntity));

  }

  public UserForAdminDto updateFromUserForAdminDtoById(Integer userId,
      UserForAdminDto userForAdminDto) {

    var user = getAndValidateById(userId);

    if (!user.getUsername().equals(userForAdminDto.getUsername())) {

      checkForExistUsername(userForAdminDto.getUsername());

    }

    user.setUsername(userForAdminDto.getUsername());
    user.setPassword(passwordEncoder.encode(userForAdminDto.getPassword()));
    user.setRoles(userForAdminDto.getRoles());

    return userForAdminFactory.convertFrom(
        update(userId, user).orElse(null)
    );

  }

  public UserEntity createFromUserDto(UserDto userDto) {

    checkForExistUsername(userDto.getUsername());

    var userEntity = userFactory.convertFrom(userDto);

    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

    userEntity.setRoles(new HashSet<>(Set.of(Role.USER)));

    return create(userEntity);

  }

  public UserEntity getByUsername(String username) {

    return dao.findByUsername(username).orElseThrow(
        () -> new NotFoundException(
            String.format("user with name: %s is not exist", username),
            HttpStatus.NOT_FOUND
        )
    );
  }


  public UserDto getUserDtoByIdAndUsername(Integer userId, String username) {

    var userEntity = getAndValidateById(userId);

    validatePermission(userEntity, username);

    return userFactory.convertFrom(userEntity);

  }

  public UserDto updateByIdAndUsername(Integer userId, String username, UserDto newUserData) {

    var userEntity = getAndValidateById(userId);

    validatePermission(userEntity, username);

    if (!userEntity.getUsername().equals(newUserData.getUsername())) {

      checkForExistUsername(newUserData.getUsername());

    }

    userEntity.setUsername(newUserData.getUsername());
    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

    return userFactory.convertFrom(
        update(userId, userEntity).orElse(null)
    );

  }

  public void deleteByIdAndUserName(Integer userId, String username) {

    var userEntity = getAndValidateById(userId);

    validatePermission(userEntity, username);

    delete(userId);

  }


  private void validatePermission(UserEntity userEntity, String username) {

    if (!username.equals(userEntity.getUsername())) {
      throw new ForbiddenException(String.format(
          "You do not have rights to get or change information about the user with id: %d",
          userEntity.getId()
      ));
    }

  }

  private UserEntity getAndValidateById(Integer userId) {

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
