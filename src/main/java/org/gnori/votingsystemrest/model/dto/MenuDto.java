package org.gnori.votingsystemrest.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.gnori.votingsystemrest.model.Item;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class MenuDto {

  Integer id;

  @NotBlank(message = "field 'name' must be not empty")
  @Length(max = 128, message = "field's 'name' length must be <= 128")
  private String name;

  @NotNull(message = "field 'itemList' must be not null")
  @Size(min = 1, max = 10, message = "field's 'itemList' size must be in interval [1; 10]")
  private List<Item> itemList;
}
