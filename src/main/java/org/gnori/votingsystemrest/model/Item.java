package org.gnori.votingsystemrest.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

  @NotBlank(message = "field 'name' must be not empty")
  @Length(max = 128, message = "length of 'name' must be less or equals than 128")
  private String name;

  @NotNull(message = "field 'price' is missed")
  @Min(value = 0 ,message = "'price' must be bigger or equals 0")
  @Max(value = 1_000_000_000, message = "'price' must be less or equals than 1.000.000.000")
  private BigDecimal price;

}
