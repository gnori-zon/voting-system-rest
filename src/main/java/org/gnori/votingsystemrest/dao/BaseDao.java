package org.gnori.votingsystemrest.dao;

import org.gnori.votingsystemrest.model.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseDao<E extends BaseEntity> extends JpaRepository<E, Integer> {

}
