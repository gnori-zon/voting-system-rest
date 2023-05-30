package org.gnori.votingsystemrest.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.BadRequestException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.impl.VoteFactory;
import org.gnori.votingsystemrest.model.dto.VoteDto;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

  private final UserDao userDao;
  private final RestaurantDao restaurantDao;
  private final VoteFactory voteFactory;

  public VoteService(
      UserDao userDao,
      RestaurantDao restaurantDao,
      VoteFactory voteFactory) {

    this.userDao = userDao;
    this.restaurantDao = restaurantDao;
    this.voteFactory = voteFactory;

  }


  public VoteDto getVote(Integer userId) {

    var user = validateAndGetUser(userId);

    if (!LocalDate.now().equals(user.getDateVote())) {

      throw new NotFoundException(
          String.format("user with id: %d is not voted today", userId),
          HttpStatus.NOT_FOUND);

    }

    return prepareVoteDtoByRestaurantId(user.getVotedFor());

  }

  public List<VoteDto> getAllVotes() {

    return restaurantDao.findAllVotes();

  }

  public VoteDto createVote(Integer userId, Integer restaurantId) {

    validateTimeVote(LocalTime.of(11,0));
    var user = validateAndGetUser(userId);

    if (LocalDate.now().equals(user.getDateVote())) {
      throw new BadRequestException(
          String.format("user with id: %d is vote already", userId),
          HttpStatus.BAD_REQUEST);
    }

    validateRestaurant(restaurantId, LocalDate.now());
    updateUserVoteParams(user, restaurantId, LocalDate.now());

    return prepareVoteDtoByRestaurantId(restaurantId);

  }

  public VoteDto updateVote(Integer userId, Integer restaurantId) {

    validateTimeVote(LocalTime.of(11,0));
    var user = validateAndGetUser(userId);
    validateRestaurant(restaurantId, LocalDate.now());
    updateUserVoteParams(user, restaurantId, LocalDate.now());

    return prepareVoteDtoByRestaurantId(restaurantId);

  }

  public void deleteVote(Integer userId) {

    updateUserVoteParams(validateAndGetUser(userId), null, null);

  }

  // utils
  private VoteDto prepareVoteDtoByRestaurantId(Integer restaurantId) {

    var restaurant = restaurantDao.findById(restaurantId).orElseThrow(
        () -> new NotFoundException(
            String.format("restaurant with id: %d is not exist", restaurantId),
            HttpStatus.NOT_FOUND)
    );
    var numberOfVotes = userDao.countByVotedForAndDateVoteEquals(restaurantId, LocalDate.now());

    return voteFactory.convertFrom(restaurant, numberOfVotes);

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
