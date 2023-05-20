package org.gnori.votingsystemrest.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
import org.gnori.votingsystemrest.service.impl.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/user/{userId}/vote")
public class VoteController {

  VoteService voteService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public RestaurantDto get(@PathVariable Integer userId) {

    return voteService.getVote(userId);

  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RestaurantDto create(@PathVariable Integer userId, @RequestParam Integer restaurantId) {

    return voteService.createVote(userId, restaurantId);

  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping
  public RestaurantDto update(@PathVariable Integer userId, @RequestParam Integer restaurantId) {

    return voteService.updateVote(userId, restaurantId);

  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping
  public void delete(@PathVariable Integer userId) {

    voteService.deleteVote(userId);

  }

}
