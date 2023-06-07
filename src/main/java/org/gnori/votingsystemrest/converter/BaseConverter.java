package org.gnori.votingsystemrest.converter;

import org.gnori.votingsystemrest.model.entity.BaseEntity;

public interface BaseConverter<D, E extends BaseEntity> {

  D convertFrom(E entity);

  E convertFrom(D dto);

}
