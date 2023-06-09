package org.gnori.votingsystemrest.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.exceptions.impl.ConflictException;
import org.gnori.votingsystemrest.error.exceptions.impl.NotFoundException;
import org.gnori.votingsystemrest.converter.impl.UserConverter;
import org.gnori.votingsystemrest.converter.impl.UserForAdminConverter;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.dto.UserForAdminDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<UserEntity, UserDao> {

  private final UserForAdminConverter userForAdminConverter;
  private final UserConverter userConverter;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserDao dao,
      UserForAdminConverter userForAdminConverter,
      PasswordEncoder passwordEncoder,
      UserConverter userConverter) {
    super(dao);
    this.userConverter = userConverter;
    this.passwordEncoder = passwordEncoder;
    this.userForAdminConverter = userForAdminConverter;
  }

  @Cacheable(cacheNames = "admin-user")
  public UserForAdminDto getUserForAdminDtoById(Integer userId) {

    var userEntity = getAndValidateById(userId);

    return userForAdminConverter.convertFrom(userEntity);

  }

  @Cacheable(cacheNames = "user")
  public UserDto getUserDtoById(Integer userId) {

    var userEntity = getAndValidateById(userId);

    return userConverter.convertFrom(userEntity);

  }

  public List<UserForAdminDto> getAllUserForAdminDto() {

    return userForAdminConverter.convertListFrom(getAll());

  }

  public List<UserDto> getAllUserDto() {

    return userConverter.convertListFrom(getAll());

  }

  public UserForAdminDto createFromUserForAdminDto(UserForAdminDto userForAdminDto) {

    checkForExistUsername(userForAdminDto.getUsername());

    var userEntity = userForAdminConverter.convertFrom(userForAdminDto);

    userEntity.setPassword(passwordEncoder.encode(userForAdminDto.getPassword()));

    return userForAdminConverter.convertFrom(create(userEntity));

  }

  public UserDto createFromUserDto(UserDto userDto) {

    checkForExistUsername(userDto.getUsername());

    var userEntity = userConverter.convertFrom(userDto);

    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

    userEntity.setRoles(new HashSet<>(Set.of(Role.USER)));

    return userConverter.convertFrom(create(userEntity));

  }

  @CachePut(cacheNames = "admin-user", key = "#userId")
  public UserForAdminDto updateFromUserForAdminDtoById(Integer userId,
      UserForAdminDto userForAdminDto) {

    var userEntity = getAndValidateById(userId);

    if (userForAdminDto.getUsername() != null &&
        !userEntity.getUsername().equals(userForAdminDto.getUsername())) {

      checkForExistUsername(userForAdminDto.getUsername());

      userEntity.setUsername(userForAdminDto.getUsername());
    }

    if (userForAdminDto.getPassword() != null) {

      userEntity.setPassword(passwordEncoder.encode(userForAdminDto.getPassword()));
    }

    if (userForAdminDto.getRoles() != null && !userForAdminDto.getRoles().isEmpty()) {

      userEntity.setRoles(userForAdminDto.getRoles());
    }

    return userForAdminConverter.convertFrom(
        update(userId, userEntity).orElse(null)
    );

  }

  @CachePut(cacheNames = "user", key = "#userId")
  public UserDto updateByIdFromUserDto(Integer userId, UserDto newUserData) {

    var userEntity = getAndValidateById(userId);
    if (newUserData.getUsername() != null &&
        !userEntity.getUsername().equals(newUserData.getUsername())) {

      checkForExistUsername(newUserData.getUsername());

      userEntity.setUsername(newUserData.getUsername());
    }

    if (newUserData.getPassword() != null) {
      userEntity.setPassword(passwordEncoder.encode(newUserData.getPassword()));
    }

    return userConverter.convertFrom(
        update(userId, userEntity).orElse(null)
    );

  }

  @CacheEvict( cacheNames = {"user", "admin-user"})
  @Override
  public void delete(Integer userId) {
    super.delete(userId);
  }

  private UserEntity getAndValidateById(Integer userId) {

    return get(userId).orElseThrow(
        () -> new NotFoundException(
            String.format("user with id: %d is not exist", userId),
            HttpStatus.NOT_FOUND
        )
    );

  }

  public UserEntity getByUsername(String username) {

    return dao.findByUsername(username).orElseThrow(
        () -> new NotFoundException(
            String.format("user with name: %s is not exist", username),
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
