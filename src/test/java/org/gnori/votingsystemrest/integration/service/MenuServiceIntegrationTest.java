package org.gnori.votingsystemrest.integration.service;

import java.math.BigDecimal;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.error.ConflictException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.MenuFactory;
import org.gnori.votingsystemrest.factory.RestaurantFactory;
import org.gnori.votingsystemrest.model.Item;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.gnori.votingsystemrest.service.impl.MenuService;
import org.gnori.votingsystemrest.service.impl.RestaurantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Integration test for MenuService and JPA")
class MenuServiceIntegrationTest {

  private final MenuService service;
  private final RestaurantService restaurantService;

  private MenuDto raw;
  private Integer rawId;
  private Integer restaurantId = 1;


  public MenuServiceIntegrationTest(
      @Autowired RestaurantDao restaurantDao, @Autowired MenuDao menuDao) {

    this.service = new MenuService(menuDao, restaurantDao, new MenuFactory());

    restaurantService = new RestaurantService(restaurantDao, new RestaurantFactory(new MenuFactory()), menuDao);

  }


  @BeforeEach
  void updateRaw(){

    raw = MenuDto.builder()
                .id(100)
                .name("new Menu")
                .itemList(
                    List.of(new Item("first item", BigDecimal.valueOf(100)),
                        new Item("second item", BigDecimal.valueOf(150)))
                )
                .build();

    rawId = raw.getId();

  }

  // GET
  @Test
  void getAllSuccess() {

    var restaurant = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurant.getId();

    service.createForRestaurant(restaurantId, raw);

    var actualAll = service.getAllMenuDto();
    var actual = actualAll.stream().findFirst().orElse(null);

    Assertions.assertNotNull(actualAll);
    Assertions.assertFalse(actualAll.isEmpty());
    Assertions.assertEquals(1, actualAll.size());
    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertEquals(raw.getItemList(), actual.getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());

    restaurantService.delete(restaurantId);

  }

  @Test
  void getMenuDtoByRestaurantIdSuccess() {

    var restaurant = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurant.getId();

    service.createForRestaurant(restaurantId, raw);

    var actual = service.getMenuDtoByRestaurantId(restaurantId);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertEquals(raw.getItemList(), actual.getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());

    restaurantService.delete(restaurantId);

  }

  @Test
  void getMenuDtoByRestaurantIdShouldThrowNotFoundExceptionByRestaurant() {

    Assertions.assertThrows(
        NotFoundException.class, () -> service.getMenuDtoByRestaurantId(restaurantId));

  }

  @Test
  void getMenuDtoByRestaurantIdShouldThrowNotFoundExceptionByMenu() {

    var restaurant = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurant.getId();

    Assertions.assertThrows(
        NotFoundException.class, () -> service.getMenuDtoByRestaurantId(restaurantId));

    restaurantService.delete(restaurantId);

  }

  // CREATE
  @Test
  void createForRestaurantSuccess() {

    var restaurant = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurant.getId();

    service.createForRestaurant(restaurantId, raw);

    var actual = service.getMenuDtoByRestaurantId(restaurantId);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertEquals(raw.getItemList(), actual.getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());

    restaurantService.delete(restaurantId);

  }

  @Test
  void createForRestaurantShouldThrowNotConflictException() {

    var restaurant = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurant.getId();

    service.createForRestaurant(restaurantId, raw);

    Assertions.assertThrows(
        ConflictException.class, () -> service.createForRestaurant(restaurantId, raw));

    restaurantService.delete(restaurantId);

  }

  @Test
  void createForRestaurantShouldThrowNotFoundException() {

    Assertions.assertThrows(
        NotFoundException.class, () -> service.createForRestaurant(restaurantId, raw));

  }

  // UPDATE
  @Test
  void updateByRestaurantIdFromMenuDtoSuccess() {

    var restaurant = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurant.getId();

    service.updateByRestaurantIdFromMenuDto(restaurantId, raw);

    var actual = service.getMenuDtoByRestaurantId(restaurantId);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertEquals(raw.getItemList(), actual.getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());

    restaurantService.delete(restaurantId);

  }

  @Test
  void updateByRestaurantIdFromMenuDtoShouldThrowNotFound() {

    Assertions.assertThrows(
        NotFoundException.class, () -> service.updateByRestaurantIdFromMenuDto(restaurantId, raw));

  }

  // DELETE
  @Test
  void deleteByRestaurantIdSuccess() {

    var restaurant = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurant.getId();

    service.createForRestaurant(restaurantId, raw);

    service.deleteByRestaurantId(restaurantId);

    Assertions.assertTrue(service.getAllMenuDto().isEmpty());

    restaurantService.delete(restaurantId);

  }

}
