package org.gnori.votingsystemrest.unit.factory;

import java.util.List;
import org.gnori.votingsystemrest.factory.BaseFactory;
import org.gnori.votingsystemrest.model.entity.BaseEntity;
import org.junit.jupiter.api.Assertions;

abstract class AbstractFactoryTest<D, E extends BaseEntity> {

  protected final BaseFactory<D, E> factory;

  AbstractFactoryTest(BaseFactory<D, E> factory ) {
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
