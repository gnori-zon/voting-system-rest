package org.gnori.votingsystemrest.controller;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.model.dto.VoteDto;
import org.gnori.votingsystemrest.service.impl.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/v1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteController {

  public static final String USER_VOTE_URL = "/users/{userId}/vote";
  public static final String VOTES_URL = "/votes";

  VoteService voteService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = USER_VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public VoteDto get(@PathVariable Integer userId) {

    return voteService.getVote(userId);

  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<VoteDto> getAll() {

    return voteService.getAllVotes();

  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = USER_VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public VoteDto create(@PathVariable Integer userId, @RequestParam Integer restaurantId) {

    return voteService.createVote(userId, restaurantId);

  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = USER_VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public VoteDto update(@PathVariable Integer userId, @RequestParam Integer restaurantId) {

    return voteService.updateVote(userId, restaurantId);

  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(USER_VOTE_URL)
  public void delete(@PathVariable Integer userId) {

    voteService.deleteVote(userId);

  }

}
