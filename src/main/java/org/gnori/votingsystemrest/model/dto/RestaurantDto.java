package org.gnori.votingsystemrest.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class RestaurantDto {

  protected Integer id;

  @NotBlank(message = "field 'name' must be not empty")
  @Length(max = 128, message = "length of 'name' must be less or equals than 128")
  protected String name;

  @Valid
  @JsonProperty("launch_menu")
  protected MenuDto launchMenu = new MenuDto();

}
