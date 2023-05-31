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

    var rawRestaurantDto = RestaurantDto.builder()
        .id(5)
        .name("r-1")
        .launchMenu(menuDto)
        .build();

    var expectedValue = RestaurantEntity.builder()
        .name(rawRestaurantDto.getName())
        .launchMenu(MenuEntity.builder()
            .name(menuDto.getName())
            .itemList(menuDto.getItemList())
            .build())
        .build();

    convertFromDtoTest(rawRestaurantDto, expectedValue);

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

    var rawRestaurantDto = RestaurantEntity.builder()
        .name("r-1")
        .launchMenu(menuDto)
        .updateMenuDate(LocalDate.now())
        .build();
    rawRestaurantDto.setId(5);

    var expectedValue = RestaurantDto.builder()
        .id(rawRestaurantDto.getId())
        .name(rawRestaurantDto.getName())
        .launchMenu(MenuDto.builder()
            .id(rawRestaurantDto.getLaunchMenu().getId())
            .name(rawRestaurantDto.getLaunchMenu().getName())
            .itemList(rawRestaurantDto.getLaunchMenu().getItemList())
            .build())
        .build();

    convertFromEntityTest(rawRestaurantDto, expectedValue);

  }

  @Test
  void convertAllFromTest() {

    var restaurantEntity1 = RestaurantEntity.builder()
        .name("r-1")
        .launchMenu(MenuEntity.builder()
            .name("m-1")
            .itemList(new ArrayList<>(List.of(new Item("i-1", BigDecimal.valueOf(100)))))
            .build())
        .updateMenuDate(LocalDate.now())
        .build();
    restaurantEntity1.setId(5);
    restaurantEntity1.getLaunchMenu().setId(5);

    var restaurantEntity2 = RestaurantEntity.builder()
        .name("r-2")
        .launchMenu(MenuEntity.builder()
            .name("m-2")
            .itemList(new ArrayList<>(List.of(new Item("i-2", BigDecimal.valueOf(200)))))
            .build())
        .updateMenuDate(LocalDate.now())
        .build();
    restaurantEntity2.getLaunchMenu().setId(6);
    restaurantEntity2.setId(6);

    var raw = new ArrayList<>(List.of(restaurantEntity1, restaurantEntity2));

    var restaurantDto1 = RestaurantDto.builder()
        .id(restaurantEntity1.getId())
        .name(restaurantEntity1.getName())
        .launchMenu(MenuDto.builder()
            .id(restaurantEntity1.getLaunchMenu().getId())
            .name(restaurantEntity1.getLaunchMenu().getName())
            .itemList(restaurantEntity1.getLaunchMenu().getItemList())
            .build())
        .build();

    var restaurantDto2 = RestaurantDto.builder()
        .id(restaurantEntity2.getId())
        .name(restaurantEntity2.getName())
        .launchMenu(MenuDto.builder()
            .id(restaurantEntity2.getLaunchMenu().getId())
            .name(restaurantEntity2.getLaunchMenu().getName())
            .itemList(restaurantEntity2.getLaunchMenu().getItemList())
            .build())
        .build();

    var expectedValue = new ArrayList<>(List.of(restaurantDto1, restaurantDto2));

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
