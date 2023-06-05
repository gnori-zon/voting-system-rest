package org.gnori.votingsystemrest.service;

import java.util.List;
import java.util.Optional;
import org.gnori.votingsystemrest.dao.BaseDao;
import org.gnori.votingsystemrest.model.entity.BaseEntity;

public abstract class AbstractService<E extends BaseEntity, D extends BaseDao<E>> implements BaseService<E>{

  protected final D dao;

  protected AbstractService(D dao) {
    this.dao = dao;
  }

  @Override
  public List<E> getAll() {
    return dao.findAll();
  }

  @Override
  public Optional<E> get(Integer id) {
    return dao.findById(id);
  }

  @Override
  public E create(E entity) {
    return dao.saveAndFlush(entity);
  }

  @Override
  public Optional<E> update(Integer id, E entity) {
    entity.setId(id);
    return Optional.of(dao.saveAndFlush(entity));
  }

  @Override
  public void delete(Integer id) {
    dao.deleteById(id);
  }
}
