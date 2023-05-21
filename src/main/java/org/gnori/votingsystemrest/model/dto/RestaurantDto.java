package org.gnori.votingsystemrest.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {

  private Integer id;

  @NotBlank(message = "field 'name' must be not empty")
  @Length(max = 128, message = "field's 'name' length must be <= 128")
  private String name;

  private MenuDto launchMenu = new MenuDto();

  private Integer numberOfVotes;

  public RestaurantDto(RestaurantEntity entity, Long numberOfVotes) {

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
}
