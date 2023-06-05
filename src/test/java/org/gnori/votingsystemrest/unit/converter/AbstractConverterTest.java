package org.gnori.votingsystemrest.unit.converter;

import java.util.List;
import org.gnori.votingsystemrest.converter.BaseConverter;
import org.gnori.votingsystemrest.model.entity.BaseEntity;
import org.junit.jupiter.api.Assertions;

abstract class AbstractConverterTest<D, E extends BaseEntity> {

  protected final BaseConverter<D, E> factory;

  AbstractConverterTest(BaseConverter<D, E> factory ) {
    this.factory = factory;
  }

  abstract void convertFromDtoTest(D raw, E expectedResult);

  void convertFromEntityTest(E raw, D expectedResult) {
    Assertions.assertEquals(expectedResult, factory.convertFrom(raw));
  }

  void convertFromListEntityTest(List<E> rawList, List<D> expectedResult) {
    Assertions.assertEquals(expectedResult, factory.convertListFrom(rawList));
  }


}
