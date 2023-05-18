package org.gnori.votingsystemrest.dao.impl;

import org.gnori.votingsystemrest.dao.BaseDao;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantDao extends BaseDao<RestaurantEntity> {

  @Query(nativeQuery = true, value = "select r.menu_id is not null from restaurant as r where r.id = :id")
  boolean isExistMenu(@Param("id") Integer id);
}
