package org.gnori.votingsystemrest.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gnori.votingsystemrest.model.entity.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  protected Integer id;

  @NotBlank(message = "field username must be not empty")
  protected String username;

  @NotBlank(message = "field password must be not empty")
  protected String password;

  protected Set<Role> roles;

  protected String token;

}
