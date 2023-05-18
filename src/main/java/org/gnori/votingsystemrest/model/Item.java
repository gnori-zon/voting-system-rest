package org.gnori.votingsystemrest.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Item {

  @NotBlank
  @Length(max = 128)
  private String name;

  @Min(0)
  @Max(Integer.MAX_VALUE)
  private BigDecimal price;

}
