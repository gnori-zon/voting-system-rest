package org.gnori.votingsystemrest.converter;

import java.util.List;
import org.gnori.votingsystemrest.model.entity.BaseEntity;

public interface ListConverter <D, E extends BaseEntity> {

  List<D> convertListFrom(List<E> listEntity);


}
