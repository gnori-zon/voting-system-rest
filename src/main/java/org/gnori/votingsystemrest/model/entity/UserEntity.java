package org.gnori.votingsystemrest.model.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity{

  @Column(unique = true, length = 128, nullable = false)
  private String username;

  @Column(length = 128, nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}, name = "uk_user_role"))
  @Column(name = "role")
  @ElementCollection(fetch = FetchType.EAGER)
  @JoinColumn
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Set<Role> roles;

  @Column(name = "voted_for")
  private Integer votedFor;

  @Column(name = "date_vote")
  private LocalDate dateVote;
}
