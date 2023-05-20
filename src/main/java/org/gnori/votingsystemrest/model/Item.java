package org.gnori.votingsystemrest.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
  @Length(max = 128, message = "field's 'name' length must be < 128")
  private String name;

  @NotBlank(message = "field 'price' must be not empty")
  @Min(value = 0 ,message = "field 'price' must be >= 0")
  @Max(value = 1_000_000_000, message = "field 'price' must be <= 1.000.000.000")
  private BigDecimal price;

}
