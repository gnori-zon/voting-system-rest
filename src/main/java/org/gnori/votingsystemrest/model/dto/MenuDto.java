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

  @NotBlank
  @Length(max = 128)
  private String name;

  @NotNull
  @Size(min = 1, max = 10)
  private List<Item> itemList;
}
