package org.gnori.votingsystemrest.dao.impl;

import java.time.LocalDate;
import java.util.List;
import org.gnori.votingsystemrest.dao.BaseDao;
import org.gnori.votingsystemrest.model.dto.VoteDto;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantDao extends BaseDao<RestaurantEntity> {

  @Query(nativeQuery = true,
      value = "select r.launch_menu_id is not null "
          + "from restaurant as r "
          + "where r.id = :id")
  boolean isExistsMenu(@Param("id") Integer id);

  boolean existsByName(String name);

  boolean existsByIdAndUpdateMenuDateEquals(Integer id, LocalDate updateMenuDate);

  @Query("select new org.gnori.votingsystemrest.model.dto.VoteDto(r, (select count(*) from UserEntity as u where u.votedFor = r.id and u.dateVote = current_date) ) "
      + "from RestaurantEntity as r "
      + "where r.updateMenuDate = current_date")
  List<VoteDto> findAllVotes();

}
