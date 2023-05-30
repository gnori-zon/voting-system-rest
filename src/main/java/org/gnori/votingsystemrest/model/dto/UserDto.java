package org.gnori.votingsystemrest.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gnori.votingsystemrest.model.dto.validation.NotEmptyIfPresent;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDto {

  public interface ValidationOfAdmin {}
  public interface AdvancedValidation {}
  public interface ValidationOfUser {}

  protected Integer id;

  @Length(max = 128, message = "length of 'username' must be less or equals than 128",
      groups = {ValidationOfUser.class, ValidationOfAdmin.class, AdvancedValidation.class})
  @NotEmpty(message = "field 'username' must be not empty",
      groups = {ValidationOfUser.class, ValidationOfAdmin.class})
  @NotEmptyIfPresent(message = "field 'username' must be not empty", groups = AdvancedValidation.class)
  protected String username;

  @Length(max = 128, message = "length of 'password' must be less or equals than 128",
      groups = {ValidationOfUser.class, ValidationOfAdmin.class, AdvancedValidation.class})
  @NotEmpty(message = "field 'password' must be not empty",
      groups = {ValidationOfUser.class, ValidationOfAdmin.class})
  @NotEmptyIfPresent(message = "field 'roles' must be not empty", groups = AdvancedValidation.class)
  protected String password;

  @NotEmpty(message = "field 'roles' must be not empty",
      groups = ValidationOfAdmin.class)
  @NotEmptyIfPresent(message = "field 'roles' must be not empty", groups = AdvancedValidation.class)
  protected Set<Role> roles;

  protected String token;

}
