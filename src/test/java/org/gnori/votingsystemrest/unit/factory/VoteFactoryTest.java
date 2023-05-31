package org.gnori.votingsystemrest.unit.factory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.gnori.votingsystemrest.factory.impl.VoteFactory;
import org.gnori.votingsystemrest.model.Item;
import org.gnori.votingsystemrest.model.dto.VoteDto;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit test for VoteFactory")
class VoteFactoryTest {

  private final VoteFactory voteFactory = new VoteFactory();

  @Test
  void convertFromTest(){

    var rawRestaurantEntity = RestaurantEntity.builder()
        .name("r-1")
        .launchMenu(MenuEntity.builder()
            .name("m-1")
            .itemList(new ArrayList<>(List.of(new Item("i-1", BigDecimal.valueOf(100)))))
            .build())
        .updateMenuDate(LocalDate.now())
        .build();
    rawRestaurantEntity.getLaunchMenu().setId(5);
    rawRestaurantEntity.setId(5);

    var numberOfVotes = 8;
    var expectedValue = new VoteDto(rawRestaurantEntity, numberOfVotes);

    Assertions.assertEquals(expectedValue, voteFactory.convertFrom(rawRestaurantEntity, numberOfVotes));
  }
}
