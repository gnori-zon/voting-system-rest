package org.gnori.votingsystemrest.dao.impl;

import java.time.LocalDate;
import java.util.Optional;
import org.gnori.votingsystemrest.dao.BaseDao;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<UserEntity> {

  Integer countByVotedForAndDateVoteEquals(Integer votedFor, LocalDate dateVote);

  boolean existsByUsername(String username);

  Optional<UserEntity> findByUsername(String username);
}
