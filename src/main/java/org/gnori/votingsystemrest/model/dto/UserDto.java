package org.gnori.votingsystemrest.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDto {

  protected Integer id;

  @Length(max = 128)
  @NotBlank(message = "field username must be not empty")
  protected String username;

  @Length(max = 128)
  @NotBlank(message = "field password must be not empty")
  protected String password;

  protected Set<Role> roles;

  protected String token;

}
