package org.gnori.votingsystemrest.integration.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.error.exceptions.impl.NotFoundException;
import org.gnori.votingsystemrest.factory.impl.VoteFactory;
import org.gnori.votingsystemrest.model.Item;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.service.impl.VoteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Integration test for VoteService and JPA")
class VoteServiceIntegrationTest {

  private final VoteService service;
  private final RestaurantDao restaurantDao;
  private final UserDao userDao;

  private Integer validRestaurantId;
  private Integer invalidRestaurantId;
  private Integer votedUserId;
  private Integer unvotedUserId;

  public VoteServiceIntegrationTest(
      @Autowired RestaurantDao restaurantDao, @Autowired UserDao userDao) {

    this.userDao = userDao;
    this.restaurantDao = restaurantDao;

    this.service = new VoteService(userDao, restaurantDao, new VoteFactory());

  }

  @BeforeEach
  void updateRaw(){
    if (validRestaurantId != null) {
      restaurantDao.deleteAllById(List.of(validRestaurantId, invalidRestaurantId));
      userDao.deleteAllById(List.of(votedUserId, unvotedUserId));
    }

    validRestaurantId = restaurantDao.saveAndFlush(
        RestaurantEntity.builder()
            .name("restaurant-1")
            .launchMenu(
                MenuEntity.builder()
                    .name("menu-1")
                    .itemList(List.of(
                        new Item("item-1", BigDecimal.valueOf(100)),
                        new Item("item-2", BigDecimal.valueOf(150))
                    ))
                    .build()
            )
            .updateMenuDate(LocalDate.now())
            .build()
        )
        .getId();

    invalidRestaurantId = restaurantDao.saveAndFlush(
        RestaurantEntity.builder()
            .name("restaurant-2")
            .launchMenu(
                MenuEntity.builder()
                    .name("menu-2")
                    .itemList(List.of(
                        new Item("item-3", BigDecimal.valueOf(200)),
                        new Item("item-4", BigDecimal.valueOf(250))
                    ))
                    .build()
            )
            .updateMenuDate(LocalDate.now().minusDays(1))
            .build()
        )
        .getId();

    votedUserId = userDao.saveAndFlush(
        UserEntity.builder()
            .username("name-1")
            .password("pass-1")
            .dateVote(LocalDate.now())
            .votedFor(validRestaurantId)
            .build()
        )
        .getId();

    unvotedUserId = userDao.saveAndFlush(
        UserEntity.builder()
            .username("name-2")
            .password("pass-2")
            .dateVote(LocalDate.now().minusDays(1))
            .votedFor(invalidRestaurantId)
            .build()
        )
        .getId();

  }

  // GET
  @Test
  void getAllVotesSuccess() {

    var actual = service.getAllVotes();

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(1, actual.size());

    Assertions.assertEquals(1, actual.get(0).getNumberOfVotes());
    Assertions.assertEquals(validRestaurantId, actual.get(0).getId());
  }

  @Test
  void getVoteSuccess() {

    var actual = service.getVoteByUserId(votedUserId);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(1, actual.getNumberOfVotes());
    Assertions.assertEquals(validRestaurantId, actual.getId());
  }

  @Test
  void getVoteShouldThrowNotFoundExceptionByUser() {

    Assertions.assertThrows(NotFoundException.class, () -> service.getVoteByUserId(100));

  }

  @Test
  void getVoteShouldThrowNotFoundExceptionByVote() {

    Assertions.assertThrows(NotFoundException.class, () -> service.getVoteByUserId(unvotedUserId));

  }

  // DELETE
  @Test
  void deleteVoteSuccess() {

    var beforeDeleteUser = userDao.findById(votedUserId).orElse(null);
    Assertions.assertNotNull(beforeDeleteUser);
    Assertions.assertNotNull(beforeDeleteUser.getDateVote());
    Assertions.assertNotNull(beforeDeleteUser.getVotedFor());

    service.deleteVoteByUserId(votedUserId);

    var afterDeleteUser = userDao.findById(votedUserId).orElse(null);
    Assertions.assertNotNull(afterDeleteUser);
    Assertions.assertNull(afterDeleteUser.getDateVote());
    Assertions.assertNull(afterDeleteUser.getVotedFor());

  }
}
