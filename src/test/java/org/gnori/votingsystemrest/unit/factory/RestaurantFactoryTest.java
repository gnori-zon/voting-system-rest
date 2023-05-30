package org.gnori.votingsystemrest.unit.factory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.gnori.votingsystemrest.factory.impl.MenuFactory;
import org.gnori.votingsystemrest.factory.impl.RestaurantFactory;
import org.gnori.votingsystemrest.model.Item;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit test for RestaurantFactory")
class RestaurantFactoryTest extends AbstractFactoryTest<RestaurantDto, RestaurantEntity> {

  RestaurantFactoryTest() {
    super(new RestaurantFactory(new MenuFactory()));
  }

  @Test
  void convertFromTest1() {

    var menuDto = MenuDto.builder()
        .id(5)
        .name("name")
        .itemList(new ArrayList<>(List.of(
            new Item("i-1", BigDecimal.valueOf(100)),
            new Item("i-2", BigDecimal.valueOf(150))))
        )
        .build();

    var raw = RestaurantDto.builder()
        .id(5)
        .name("r-1")
        .launchMenu(menuDto)
        .build();

    var expectedValue = RestaurantEntity.builder()
        .name(raw.getName())
        .launchMenu(MenuEntity.builder()
            .name(menuDto.getName())
            .itemList(menuDto.getItemList())
            .build())
        .build();

    convertFromDtoTest(raw, expectedValue);

  }

  @Test
  void convertFromTest2() {

    var menuDto = MenuEntity.builder()
        .name("name")
        .itemList(new ArrayList<>(List.of(
            new Item("i-1", BigDecimal.valueOf(100)),
            new Item("i-2", BigDecimal.valueOf(150))))
        )
        .build();
    menuDto.setId(5);

    var raw = RestaurantEntity.builder()
        .name("r-1")
        .launchMenu(menuDto)
        .updateMenuDate(LocalDate.now())
        .build();
    raw.setId(5);

    var expectedValue = RestaurantDto.builder()
        .id(raw.getId())
        .name(raw.getName())
        .launchMenu(MenuDto.builder()
            .id(raw.getLaunchMenu().getId())
            .name(raw.getLaunchMenu().getName())
            .itemList(raw.getLaunchMenu().getItemList())
            .build())
        .build();

    convertFromEntityTest(raw, expectedValue);

  }

  @Test
  void convertAllFromTest() {

    var entity1 = RestaurantEntity.builder()
        .name("r-1")
        .launchMenu(MenuEntity.builder()
            .name("m-1")
            .itemList(new ArrayList<>(List.of(new Item("i-1", BigDecimal.valueOf(100)))))
            .build())
        .updateMenuDate(LocalDate.now())
        .build();
    entity1.setId(5);
    entity1.getLaunchMenu().setId(5);

    var entity2 = RestaurantEntity.builder()
        .name("r-2")
        .launchMenu(MenuEntity.builder()
            .name("m-2")
            .itemList(new ArrayList<>(List.of(new Item("i-2", BigDecimal.valueOf(200)))))
            .build())
        .updateMenuDate(LocalDate.now())
        .build();
    entity2.getLaunchMenu().setId(6);
    entity2.setId(6);

    var raw = new ArrayList<>(List.of(entity1, entity2));

    var dto1 = RestaurantDto.builder()
        .id(entity1.getId())
        .name(entity1.getName())
        .launchMenu(MenuDto.builder()
            .id(entity1.getLaunchMenu().getId())
            .name(entity1.getLaunchMenu().getName())
            .itemList(entity1.getLaunchMenu().getItemList())
            .build())
        .build();

    var dto2 = RestaurantDto.builder()
        .id(entity2.getId())
        .name(entity2.getName())
        .launchMenu(MenuDto.builder()
            .id(entity2.getLaunchMenu().getId())
            .name(entity2.getLaunchMenu().getName())
            .itemList(entity2.getLaunchMenu().getItemList())
            .build())
        .build();

    var expectedValue = new ArrayList<>(List.of(dto1, dto2));

    convertFromListEntityTest(raw, expectedValue);

  }


  @Override
  void convertFromDtoTest(RestaurantDto raw, RestaurantEntity expectedResult) {

    var actual = factory.convertFrom(raw);

    Assertions.assertEquals(expectedResult.getName(), actual.getName());
    Assertions.assertEquals(expectedResult.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(expectedResult.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());

    Assertions.assertNull(actual.getId());
    Assertions.assertNull(actual.getUpdateMenuDate());
    Assertions.assertNull(actual.getLaunchMenu().getId());


  }
}
