package org.gnori.votingsystemrest.factory;

import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.model.dto.VoteDto;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteFactory {

  public VoteDto convertFrom(RestaurantEntity restaurantEntity, Integer numberOfVotes) {

    if (restaurantEntity == null) return null;

    return new VoteDto(restaurantEntity, numberOfVotes);
  }

}
