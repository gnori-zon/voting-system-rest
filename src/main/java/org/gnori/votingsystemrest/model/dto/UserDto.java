package org.gnori.votingsystemrest.model.dto;

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

  protected String username;

  protected String password;

  protected Set<Role> roles;

}
