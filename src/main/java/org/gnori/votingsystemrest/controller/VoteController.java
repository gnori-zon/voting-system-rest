package org.gnori.votingsystemrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.log.annotation.LogMethodExecutionTime;
import org.gnori.votingsystemrest.model.dto.UserDto;
import org.gnori.votingsystemrest.model.dto.VoteDto;
import org.gnori.votingsystemrest.service.impl.VoteService;
import org.gnori.votingsystemrest.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth")
public class VoteController {

  public static final String USER_VOTE_URL = "/users/vote";
  public static final String VOTES_URL = "/votes";

  private static final String AUTH_HEADER = "Authorization";

  VoteService voteService;
  AuthenticationService<UserDto, Integer, String> authenticationService;

  @Operation(description = "get vote by user id")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = USER_VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public VoteDto get(@RequestHeader(AUTH_HEADER) String token) {

    var userId = authenticationService.getUserIdFrom(token);

    return voteService.getVoteByUserId(userId);

  }

  @Operation(description = "get all votes")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<VoteDto> getAll() {

    return voteService.getAllVotes();

  }

  @Operation(description = "vote by user and restaurant ids")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = USER_VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public VoteDto create(
      @RequestHeader(AUTH_HEADER) String token,
      @RequestParam Integer restaurantId
  ) {

    var userId = authenticationService.getUserIdFrom(token);

    return voteService.createVoteByUserIdAndRestaurantId(userId, restaurantId);

  }

  @Operation(description = "change vote by user and restaurant ids")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = USER_VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public VoteDto update(
      @RequestHeader(AUTH_HEADER) String token,
      @RequestParam Integer restaurantId
  ) {

    var userId = authenticationService.getUserIdFrom(token);

    return voteService.updateVoteByUserIdAndRestaurantId(userId, restaurantId);

  }

  @Operation(description = "delete vote by user id")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(USER_VOTE_URL)
  public void delete(@RequestHeader(AUTH_HEADER) String token) {

    var userId = authenticationService.getUserIdFrom(token);

    voteService.deleteVoteByUserId(userId);

  }

}
