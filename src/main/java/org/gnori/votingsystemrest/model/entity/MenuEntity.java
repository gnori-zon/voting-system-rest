package org.gnori.votingsystemrest.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gnori.votingsystemrest.model.Item;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity extends BaseEntity{

  @Column(length = 128)
  private String name;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "item_list")
  private List<Item> itemList;
}
