package org.gnori.votingsystemrest.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class VoteDto extends RestaurantDto {

  @JsonProperty("number_of_votes")
  protected Integer numberOfVotes;

  public VoteDto(RestaurantEntity restaurantEntity, Integer numberOfVotes) {

    id = restaurantEntity.getId();
    name = restaurantEntity.getName();
    launchMenu = new MenuDto();

    if (restaurantEntity.getLaunchMenu() != null) {
      launchMenu.setId(restaurantEntity.getLaunchMenu().getId());
      launchMenu.setName(restaurantEntity.getLaunchMenu().getName());
      launchMenu.setItemList(restaurantEntity.getLaunchMenu().getItemList());
    }

    this.numberOfVotes = numberOfVotes != null ? numberOfVotes : 0;

  }
}
