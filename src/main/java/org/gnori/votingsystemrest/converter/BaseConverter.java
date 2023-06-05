package org.gnori.votingsystemrest.converter;

import java.util.List;
import org.gnori.votingsystemrest.model.entity.BaseEntity;

public interface BaseConverter<D, E extends BaseEntity> {

  D convertFrom(E entity);

  E convertFrom(D dto);

  List<D> convertListFrom(List<E> listEntity);

}
