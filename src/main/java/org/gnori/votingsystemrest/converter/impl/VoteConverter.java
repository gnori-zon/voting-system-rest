package org.gnori.votingsystemrest.converter.impl;

import org.gnori.votingsystemrest.model.dto.VoteDto;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
public class VoteConverter {

  public VoteDto convertFrom(RestaurantEntity restaurantEntity, Integer numberOfVotes) {

    if (restaurantEntity == null) return null;

    return new VoteDto(restaurantEntity, numberOfVotes);
  }

}
