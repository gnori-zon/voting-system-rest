package org.gnori.votingsystemrest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto extends RestaurantDto {

  protected Integer numberOfVotes;

  public VoteDto(RestaurantEntity entity, Integer numberOfVotes) {

    id = entity.getId();
    name = entity.getName();
    launchMenu = new MenuDto();

    if (entity.getLaunchMenu() != null) {
      launchMenu.setId(entity.getLaunchMenu().getId());
      launchMenu.setName(entity.getLaunchMenu().getName());
      launchMenu.setItemList(entity.getLaunchMenu().getItemList());
    }

    this.numberOfVotes = numberOfVotes != null ? numberOfVotes : 0;

  }
}
