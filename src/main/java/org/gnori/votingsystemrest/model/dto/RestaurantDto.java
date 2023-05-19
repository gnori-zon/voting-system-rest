package org.gnori.votingsystemrest.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class RestaurantDto {

  private Integer id;

  @NotBlank(message = "field 'name' must be not empty")
  @Length(max = 128, message = "field's 'name' length must be <= 128")
  private String name;

  private MenuDto launchMenu;
}
