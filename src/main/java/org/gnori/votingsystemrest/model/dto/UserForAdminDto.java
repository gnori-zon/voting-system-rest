package org.gnori.votingsystemrest.model.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForAdminDto extends UserDto{

  protected Integer votedFor;

  protected LocalDate dateVote;
}
