package org.gnori.votingsystemrest.service;

import java.util.List;
import java.util.Optional;
import org.gnori.votingsystemrest.model.entity.BaseEntity;

public interface BaseService<E extends BaseEntity> {

  List<E> getAll();
  Optional<E> get(Integer id);
  E create(E entity);
  Optional<E> update(Integer id, E entity);
  void delete(Integer id);
}
