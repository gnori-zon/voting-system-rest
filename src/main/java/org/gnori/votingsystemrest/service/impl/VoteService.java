package org.gnori.votingsystemrest.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.BadRequestException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.RestaurantFactory;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

  private final UserDao userDao;
  private final RestaurantDao restaurantDao;
  private final RestaurantFactory restaurantFactory;

  public VoteService(
      UserDao userDao,
      RestaurantDao restaurantDao,
      RestaurantFactory restaurantFactory) {

    this.userDao = userDao;
    this.restaurantDao = restaurantDao;
    this.restaurantFactory = restaurantFactory;

  }


  public RestaurantDto getVote(Integer userId) {

    var user = validateAndGetUser(userId);

    if (!LocalDate.now().equals(user.getDateVote())) {

      throw new NotFoundException(
          String.format("user with id: %d is not voted today", userId),
          HttpStatus.NOT_FOUND);

    }

    return prepareRestaurantDto(user.getVotedFor());

  }

  public List<RestaurantDto> getAllVotes() {

    return restaurantDao.findAllVotes()
        .stream()
        .map(array -> new RestaurantDto((RestaurantEntity) array[0], (Long) array[1]))
        .toList();

  }

  public RestaurantDto createVote(Integer userId, Integer restaurantId) {

    validateTimeVote(LocalTime.of(11,0));
    var user = validateAndGetUser(userId);

    if (LocalDate.now().equals(user.getDateVote())) {
      throw new BadRequestException(
          String.format("user with id: %d is vote already", userId),
          HttpStatus.BAD_REQUEST);
    }

    validateRestaurant(restaurantId, LocalDate.now());
    updateUserVoteParams(user, restaurantId, LocalDate.now());

    return prepareRestaurantDto(restaurantId);

  }

  public RestaurantDto updateVote(Integer userId, Integer restaurantId) {

    validateTimeVote(LocalTime.of(11,0));
    var user = validateAndGetUser(userId);
    validateRestaurant(restaurantId, LocalDate.now());
    updateUserVoteParams(user, restaurantId, LocalDate.now());

    return prepareRestaurantDto(restaurantId);

  }

  public void deleteVote(Integer userId) {

    updateUserVoteParams(validateAndGetUser(userId), null, null);

  }

  // utils
  private RestaurantDto prepareRestaurantDto(Integer restaurantId) {

    var restaurant = restaurantDao.findById(restaurantId).orElseThrow(
        () -> new NotFoundException(
            String.format("restaurant with id: %d is not exist", restaurantId),
            HttpStatus.NOT_FOUND)
    );

    var restaurantDto = restaurantFactory.convertFrom(restaurant);
    var numberOfVotes = userDao.countByVotedForAndDateVoteEquals(restaurantId, LocalDate.now());

    restaurantDto.setNumberOfVotes(numberOfVotes);

    return restaurantDto;

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