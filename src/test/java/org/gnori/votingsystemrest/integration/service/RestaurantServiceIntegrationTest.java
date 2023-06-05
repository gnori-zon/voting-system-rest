package org.gnori.votingsystemrest.integration.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.error.exceptions.impl.ConflictException;
import org.gnori.votingsystemrest.error.exceptions.impl.NotFoundException;
import org.gnori.votingsystemrest.converter.impl.MenuConverter;
import org.gnori.votingsystemrest.converter.impl.RestaurantConverter;
import org.gnori.votingsystemrest.model.Item;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
import org.gnori.votingsystemrest.service.impl.RestaurantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Integration test for RestaurantService and JPA")
class RestaurantServiceIntegrationTest {

  private final RestaurantService service;

  private RestaurantDto rawRestaurantDto;
  private Integer rawId;
  private Integer menuIdFromRaw;

  RestaurantServiceIntegrationTest(
      @Autowired RestaurantDao restaurantDao, @Autowired MenuDao menuDao) {

    var factory = new RestaurantConverter(new MenuConverter());
    this.service = new RestaurantService(menuDao, restaurantDao, factory);

  }

  @BeforeEach
  void updateRaw(){

    rawRestaurantDto = RestaurantDto.builder()
        .id(5)
        .name("new Restaurant")
        .launchMenu(
            MenuDto.builder()
                .id(5)
                .name("new Menu")
                .itemList(
                    List.of(new Item("first item", BigDecimal.valueOf(100)),
                    new Item("second item", BigDecimal.valueOf(150)))
                )
                .build()
        )
        .build();

    rawId = rawRestaurantDto.getId();
    menuIdFromRaw = rawRestaurantDto.getLaunchMenu().getId();

  }

  // GET
  @Test
  void getAllSuccess(){

    var idCreatedRestaurant = service.createFromRestaurantDto(rawRestaurantDto).getId();

    var actualAll = service.getAllRestaurantDto();
    var actual = actualAll.stream().findFirst().orElse(null);

    Assertions.assertNotNull(actualAll);
    Assertions.assertFalse(actualAll.isEmpty());
    Assertions.assertEquals(1, actualAll.size());
    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawRestaurantDto.getName(), actual.getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());
    Assertions.assertNotEquals(menuIdFromRaw, actual.getLaunchMenu().getId());

    service.delete(idCreatedRestaurant);

  }

  @Test
  void getRestaurantDtoByIdSuccess(){

    var idCreatedRestaurant = service.createFromRestaurantDto(rawRestaurantDto).getId();

    var actual = service.getRestaurantDtoById(idCreatedRestaurant);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawRestaurantDto.getName(), actual.getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());
    Assertions.assertNotEquals(menuIdFromRaw, actual.getLaunchMenu().getId());

    service.delete(idCreatedRestaurant);

  }

  @Test
  void getRestaurantDtoByIdShouldThrowNotFoundException(){

    Assertions.assertThrows(NotFoundException.class, () -> service.getRestaurantDtoById(rawId));

  }

  // CREATE
  @Test
  void createFromRestaurantDtoTestSuccess() {

    service.createFromRestaurantDto(rawRestaurantDto);

    var actual = service.getAll().stream().findFirst().orElse(null);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawRestaurantDto.getName(), actual.getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());
    Assertions.assertEquals(LocalDate.now(), actual.getUpdateMenuDate());

    Assertions.assertNotEquals(rawId, actual.getId());
    Assertions.assertNotEquals(menuIdFromRaw, actual.getLaunchMenu().getId());

    service.delete(actual.getId());
  }

  @Test
  void createFromRestaurantDtoTestShouldThrowConflictException() {

    var idCreatedRestaurant = service.createFromRestaurantDto(rawRestaurantDto).getId();

    Assertions.assertThrows(ConflictException.class,
        () -> service.createFromRestaurantDto(rawRestaurantDto));

    service.delete(idCreatedRestaurant);
  }

  // UPDATE
  @Test
  void updateByIdFromRestaurantDtoSuccess() {

    var createdMenu = rawRestaurantDto.getLaunchMenu();

    rawRestaurantDto.setLaunchMenu(null);
    var idCreatedRestaurant = service.createFromRestaurantDto(rawRestaurantDto).getId();

    var actual = service.getAll().stream().findFirst().orElse(null);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawRestaurantDto.getName(), actual.getName());
    Assertions.assertNull(actual.getLaunchMenu());
    Assertions.assertNull(actual.getUpdateMenuDate());

    rawRestaurantDto.setName("newName");
    rawRestaurantDto.setLaunchMenu(createdMenu);
    service.updateByIdFromRestaurantDto(idCreatedRestaurant, rawRestaurantDto);

    actual = service.getAll().stream().findFirst().orElse(null);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(rawRestaurantDto.getName(), actual.getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(
        rawRestaurantDto.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());
    Assertions.assertEquals(LocalDate.now(), actual.getUpdateMenuDate());

    service.delete(idCreatedRestaurant);

  }

  @Test
  void updateByIdFromRestaurantDtoShouldThrowNotFoundException() {

    Assertions.assertThrows(NotFoundException.class,
        () -> service.updateByIdFromRestaurantDto(rawId, rawRestaurantDto));

  }

  @Test
  void updateByIdFromRestaurantDtoShouldThrowConflictException() {

    var nameFirstRestaurant = rawRestaurantDto.getName();
    var idFirstRestaurant = service.createFromRestaurantDto(rawRestaurantDto).getId();

    rawRestaurantDto.setName("newSecondName");
    var idSecondRestaurant = service.createFromRestaurantDto(rawRestaurantDto).getId();

    rawRestaurantDto.setName(nameFirstRestaurant);

    Assertions.assertThrows(ConflictException.class,
        () -> service.updateByIdFromRestaurantDto(idSecondRestaurant, rawRestaurantDto));

    service.delete(idFirstRestaurant);
    service.delete(idSecondRestaurant);

  }

}
