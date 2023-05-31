package org.gnori.votingsystemrest.integration.service;

import java.math.BigDecimal;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.error.exceptions.impl.ConflictException;
import org.gnori.votingsystemrest.error.exceptions.impl.NotFoundException;
import org.gnori.votingsystemrest.factory.impl.MenuFactory;
import org.gnori.votingsystemrest.factory.impl.RestaurantFactory;
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

  private MenuDto rawMenuDto;
  private Integer rawId;
  private Integer restaurantId = 1;


  public MenuServiceIntegrationTest(
      @Autowired RestaurantDao restaurantDao, @Autowired MenuDao menuDao) {

    this.service = new MenuService(menuDao, restaurantDao, new MenuFactory());

    restaurantService = new RestaurantService(menuDao, restaurantDao,
        new RestaurantFactory(new MenuFactory()) );

  }


  @BeforeEach
  void updateRaw(){

    rawMenuDto = MenuDto.builder()
                .id(100)
                .name("new Menu")
                .itemList(
                    List.of(new Item("first item", BigDecimal.valueOf(100)),
                        new Item("second item", BigDecimal.valueOf(150)))
                )
                .build();

    rawId = rawMenuDto.getId();

  }

  // GET
  @Test
  void getAllSuccess() {

    var restaurantEntity = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurantEntity.getId();

    service.createByRestaurantIdFromMenuDto(restaurantId, rawMenuDto);

    var actualAll = service.getAllMenuDto();
    var actual = actualAll.stream().findFirst().orElse(null);

    Assertions.assertNotNull(actualAll);
    Assertions.assertFalse(actualAll.isEmpty());
    Assertions.assertEquals(1, actualAll.size());
    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawMenuDto.getName(), actual.getName());
    Assertions.assertEquals(rawMenuDto.getItemList(), actual.getItemList());

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

    service.createByRestaurantIdFromMenuDto(restaurantId, rawMenuDto);

    var actual = service.getMenuDtoByRestaurantId(restaurantId);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawMenuDto.getName(), actual.getName());
    Assertions.assertEquals(rawMenuDto.getItemList(), actual.getItemList());

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

    var restaurantEntity = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurantEntity.getId();

    Assertions.assertThrows(
        NotFoundException.class, () -> service.getMenuDtoByRestaurantId(restaurantId));

    restaurantService.delete(restaurantId);

  }

  // CREATE
  @Test
  void createForRestaurantSuccess() {

    var restaurantEntity = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurantEntity.getId();

    service.createByRestaurantIdFromMenuDto(restaurantId, rawMenuDto);

    var actual = service.getMenuDtoByRestaurantId(restaurantId);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawMenuDto.getName(), actual.getName());
    Assertions.assertEquals(rawMenuDto.getItemList(), actual.getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());

    restaurantService.delete(restaurantId);

  }

  @Test
  void createForRestaurantShouldThrowNotConflictException() {

    var restaurantEntity = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurantEntity.getId();

    service.createByRestaurantIdFromMenuDto(restaurantId, rawMenuDto);

    Assertions.assertThrows(
        ConflictException.class,
        () -> service.createByRestaurantIdFromMenuDto(restaurantId, rawMenuDto));

    restaurantService.delete(restaurantId);

  }

  @Test
  void createForRestaurantShouldThrowNotFoundException() {

    Assertions.assertThrows(
        NotFoundException.class,
        () -> service.createByRestaurantIdFromMenuDto(restaurantId, rawMenuDto));

  }

  // UPDATE
  @Test
  void updateByRestaurantIdFromMenuDtoSuccess() {

    var restaurantEntity = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurantEntity.getId();

    service.updateByRestaurantIdFromMenuDto(restaurantId, rawMenuDto);

    var actual = service.getMenuDtoByRestaurantId(restaurantId);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawMenuDto.getName(), actual.getName());
    Assertions.assertEquals(rawMenuDto.getItemList(), actual.getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());

    restaurantService.delete(restaurantId);

  }

  @Test
  void updateByRestaurantIdFromMenuDtoShouldThrowNotFound() {

    Assertions.assertThrows(
        NotFoundException.class,
        () -> service.updateByRestaurantIdFromMenuDto(restaurantId, rawMenuDto));

  }

  // DELETE
  @Test
  void deleteByRestaurantIdSuccess() {

    var restaurantEntity = restaurantService.create(
        RestaurantEntity.builder()
            .name("restaurant")
            .build());

    restaurantId = restaurantEntity.getId();

    service.createByRestaurantIdFromMenuDto(restaurantId, rawMenuDto);

    service.deleteByRestaurantId(restaurantId);

    Assertions.assertTrue(service.getAllMenuDto().isEmpty());

    restaurantService.delete(restaurantId);

  }

}
