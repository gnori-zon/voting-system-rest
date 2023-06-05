package org.gnori.votingsystemrest.unit.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.gnori.votingsystemrest.converter.impl.MenuConverter;
import org.gnori.votingsystemrest.model.Item;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit test for MenuFactory")
class MenuConverterTest extends AbstractConverterTest<MenuDto, MenuEntity> {

  public MenuConverterTest() {
    super(new MenuConverter());
  }

  @Test
  void convertFromTest1() {

    var rawMenu = MenuDto.builder()
        .id(5)
        .name("name")
        .itemList(new ArrayList<>(List.of(
                new Item("i-1", BigDecimal.valueOf(100)),
                new Item("i-2", BigDecimal.valueOf(150))))
        )
        .build();

    var expectedValue = MenuEntity.builder()
        .name(rawMenu.getName())
        .itemList(rawMenu.getItemList())
        .build();

    convertFromDtoTest(rawMenu, expectedValue);

  }

  @Test
  void convertFromTest2() {

    var rawMenu = MenuEntity.builder()
        .name("name")
        .itemList(new ArrayList<>(List.of(
            new Item("i-1", BigDecimal.valueOf(100)),
            new Item("i-2", BigDecimal.valueOf(150))))
        )
        .build();
    rawMenu.setId(5);

    var expectedValue = MenuDto.builder()
        .id(rawMenu.getId())
        .name(rawMenu.getName())
        .itemList(rawMenu.getItemList())
        .build();

    convertFromEntityTest(rawMenu, expectedValue);

  }
  @Test
  void convertAllFromTest() {

    var menuEntity1 = MenuEntity.builder()
        .name("name1")
        .itemList(new ArrayList<>(List.of(
            new Item("i-1", BigDecimal.valueOf(100)),
            new Item("i-2", BigDecimal.valueOf(150))))
        )
        .build();
    menuEntity1.setId(5);

    var menuEntity2 = MenuEntity.builder()
        .name("name2")
        .itemList(new ArrayList<>(List.of(
            new Item("i-3", BigDecimal.valueOf(150)),
            new Item("i-4", BigDecimal.valueOf(200))))
        )
        .build();
    menuEntity2.setId(6);

    var raw = new ArrayList<>(List.of(menuEntity1, menuEntity2));

    var menuDto1 = MenuDto.builder()
        .id(menuEntity1.getId())
        .name(menuEntity1.getName())
        .itemList(menuEntity1.getItemList())
        .build();

    var menuDto2 = MenuDto.builder()
        .id(menuEntity2.getId())
        .name(menuEntity2.getName())
        .itemList(menuEntity2.getItemList())
        .build();

    var expectedValue = new ArrayList<>(List.of(menuDto1, menuDto2));

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
