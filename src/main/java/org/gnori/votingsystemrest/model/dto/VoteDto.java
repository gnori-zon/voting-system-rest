package org.gnori.votingsystemrest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto extends RestaurantDto{

  protected Integer numberOfVotes;


  /** necessary and must place first, since the database, when calling count(),
   *  returns a Long in {@link RestaurantDao#findAllVotes()}*/
  public VoteDto(RestaurantEntity entity, Long numberOfVotes) {

    id = entity.getId();
    name = entity.getName();
    launchMenu = new MenuDto();

    if (entity.getLaunchMenu() != null) {
      launchMenu.setId(entity.getLaunchMenu().getId());
      launchMenu.setName(entity.getLaunchMenu().getName());
      launchMenu.setItemList(entity.getLaunchMenu().getItemList());
    }

    this.numberOfVotes = numberOfVotes != null ? numberOfVotes.intValue() : 0;
  }

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
