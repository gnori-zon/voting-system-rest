package org.gnori.votingsystemrest.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gnori.votingsystemrest.model.Item;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {

  protected Integer id;

  @NotBlank(message = "field 'name' must be not empty")
  @Length(max = 128, message = "field's 'name' length must be <= 128")
  protected String name;

  @NotNull(message = "field 'itemList' must be not null")
  @Size(min = 1, max = 10, message = "field's 'itemList' size must be in interval [1; 10]")
  protected List<Item> itemList;

}
