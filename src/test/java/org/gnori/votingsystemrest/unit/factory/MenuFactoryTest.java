package org.gnori.votingsystemrest.unit.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.gnori.votingsystemrest.factory.MenuFactory;
import org.gnori.votingsystemrest.model.Item;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit test for MenuFactory")
class MenuFactoryTest extends AbstractFactoryTest<MenuDto, MenuEntity> {

  public MenuFactoryTest() {
    super(new MenuFactory());
  }

  @Test
  void convertFromTest1() {

    var raw = MenuDto.builder()
        .id(5)
        .name("name")
        .itemList(new ArrayList<>(List.of(
                new Item("i-1", BigDecimal.valueOf(100)),
                new Item("i-2", BigDecimal.valueOf(150))))
        )
        .build();

    var expectedValue = MenuEntity.builder()
        .name(raw.getName())
        .itemList(raw.getItemList())
        .build();

    convertFromDtoTest(raw, expectedValue);

  }

  @Test
  void convertFromTest2() {

    var raw = MenuEntity.builder()
        .name("name")
        .itemList(new ArrayList<>(List.of(
            new Item("i-1", BigDecimal.valueOf(100)),
            new Item("i-2", BigDecimal.valueOf(150))))
        )
        .build();
    raw.setId(5);

    var expectedValue = MenuDto.builder()
        .id(raw.getId())
        .name(raw.getName())
        .itemList(raw.getItemList())
        .build();

    convertFromEntityTest(raw, expectedValue);

  }
  @Test
  void convertAllFromTest() {

    var entity1 = MenuEntity.builder()
        .name("name1")
        .itemList(new ArrayList<>(List.of(
            new Item("i-1", BigDecimal.valueOf(100)),
            new Item("i-2", BigDecimal.valueOf(150))))
        )
        .build();
    entity1.setId(5);

    var entity2 = MenuEntity.builder()
        .name("name2")
        .itemList(new ArrayList<>(List.of(
            new Item("i-3", BigDecimal.valueOf(150)),
            new Item("i-4", BigDecimal.valueOf(200))))
        )
        .build();
    entity2.setId(6);

    var raw = new ArrayList<>(List.of(entity1, entity2));

    var dto1 = MenuDto.builder()
        .id(entity1.getId())
        .name(entity1.getName())
        .itemList(entity1.getItemList())
        .build();

    var dto2 = MenuDto.builder()
        .id(entity2.getId())
        .name(entity2.getName())
        .itemList(entity2.getItemList())
        .build();

    var expectedValue = new ArrayList<>(List.of(dto1, dto2));

    convertFromListEntityTest(raw, expectedValue);

  }

  @Override
  void convertFromDtoTest(MenuDto raw, MenuEntity expectedResult) {

    var actual = factory.convertFrom(raw);

    Assertions.assertEquals(expectedResult.getItemList(), actual.getItemList());
    Assertions.assertEquals(expectedResult.getName(), actual.getName());
    Assertions.assertNull(actual.getId());

  }
}
