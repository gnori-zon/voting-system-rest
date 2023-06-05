package org.gnori.votingsystemrest.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.exceptions.impl.BadRequestException;
import org.gnori.votingsystemrest.error.exceptions.impl.NotFoundException;
import org.gnori.votingsystemrest.converter.impl.VoteConverter;
import org.gnori.votingsystemrest.model.dto.VoteDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "vote")
public class VoteService {

  private final UserDao userDao;
  private final RestaurantDao restaurantDao;
  private final VoteConverter voteConverter;

  public VoteService(
      UserDao userDao,
      RestaurantDao restaurantDao,
      VoteConverter voteConverter) {

    this.userDao = userDao;
    this.restaurantDao = restaurantDao;
    this.voteConverter = voteConverter;

  }
  @Cacheable
  public VoteDto getVoteByUserId(Integer userId) {

    var userEntity = validateAndGetUser(userId);

    if (!LocalDate.now().equals(userEntity.getDateVote())) {

      throw new NotFoundException(
          String.format("user with id: %d is not voted today", userId),
          HttpStatus.NOT_FOUND);

    }

    return prepareVoteDtoByRestaurantId(userEntity.getVotedFor());

  }

  public List<VoteDto> getAllVotes() {

    return restaurantDao.findAllVotes();

  }

  @CachePut(key = "#userId")
  public VoteDto createVoteByUserIdAndRestaurantId(Integer userId, Integer restaurantId) {

    validateTimeVote(LocalTime.of(11,0));
    var userEntity = validateAndGetUser(userId);

    if (LocalDate.now().equals(userEntity.getDateVote())) {
      throw new BadRequestException(
          String.format("user with id: %d is vote already", userId),
          HttpStatus.BAD_REQUEST);
    }

    validateRestaurant(restaurantId, LocalDate.now());
    updateUserVoteParams(userEntity, restaurantId, LocalDate.now());

    return prepareVoteDtoByRestaurantId(restaurantId);

  }
  @CachePut(key = "#userId")
  public VoteDto updateVoteByUserIdAndRestaurantId(Integer userId, Integer restaurantId) {

    validateTimeVote(LocalTime.of(11,0));
    var userEntity = validateAndGetUser(userId);
    validateRestaurant(restaurantId, LocalDate.now());
    updateUserVoteParams(userEntity, restaurantId, LocalDate.now());

    return prepareVoteDtoByRestaurantId(restaurantId);

  }

  @CacheEvict("vote")
  public void deleteVoteByUserId(Integer userId) {

    updateUserVoteParams(validateAndGetUser(userId), null, null);

  }

  // utils
  private VoteDto prepareVoteDtoByRestaurantId(Integer restaurantId) {

    var restaurantEntity = restaurantDao.findById(restaurantId).orElseThrow(
        () -> new NotFoundException(
            String.format("restaurant with id: %d is not exist", restaurantId),
            HttpStatus.NOT_FOUND)
    );
    var numberOfVotes = userDao.countByVotedForAndDateVoteEquals(restaurantId, LocalDate.now());

    return voteConverter.convertFrom(restaurantEntity, numberOfVotes);

  }

  private void validateRestaurant(Integer restaurantId, LocalDate date) {

    if (!restaurantDao.existsByIdAndUpdateMenuDateEquals(restaurantId, date)) {
      throw new NotFoundException(
          String.format("restaurant with id: %d is not exist or him menu not updated %s ", restaurantId, date),
          HttpStatus.NOT_FOUND);
    }

  }

  private void validateTimeVote(LocalTime time) {

    if (LocalTime.now().isAfter(time)) {
      throw new BadRequestException(
          String.format("It's too late to vote after %s", time),
          HttpStatus.BAD_REQUEST);
    }

  }

  private UserEntity validateAndGetUser(Integer userId) {

    return userDao.findById(userId).orElseThrow(
        () -> new NotFoundException(
            String.format("user with id: %d is not exist", userId),
            HttpStatus.NOT_FOUND)
    );

  }

  private void updateUserVoteParams(UserEntity user, Integer newVotedFor, LocalDate newDateVote) {

    user.setVotedFor(newVotedFor);
    user.setDateVote(newDateVote);
    userDao.saveAndFlush(user);

  }

}
