package org.gnori.votingsystemrest.integration.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.error.ConflictException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.MenuFactory;
import org.gnori.votingsystemrest.factory.RestaurantFactory;
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

  private RestaurantDto raw;
  private Integer rawId;
  private Integer menuIdFromRaw;

  RestaurantServiceIntegrationTest(
      @Autowired RestaurantDao restaurantDao, @Autowired MenuDao menuDao) {

    var factory = new RestaurantFactory(new MenuFactory());
    this.service = new RestaurantService(menuDao, restaurantDao, factory);

  }

  @BeforeEach
  void updateRaw(){

    raw = RestaurantDto.builder()
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

    rawId = raw.getId();
    menuIdFromRaw = raw.getLaunchMenu().getId();

  }

  // GET
  @Test
  void getAllSuccess(){

    var id = service.createFromRestaurantDto(raw).getId();

    var actualAll = service.getAllRestaurantDto();
    var actual = actualAll.stream().findFirst().orElse(null);

    Assertions.assertNotNull(actualAll);
    Assertions.assertFalse(actualAll.isEmpty());
    Assertions.assertEquals(1, actualAll.size());
    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertEquals(raw.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(raw.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(raw.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());
    Assertions.assertNotEquals(menuIdFromRaw, actual.getLaunchMenu().getId());

    service.delete(id);

  }

  @Test
  void getRestaurantDtoByIdSuccess(){

    var id = service.createFromRestaurantDto(raw).getId();

    var actual = service.getRestaurantDtoById(id);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertEquals(raw.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(raw.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(raw.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());

    Assertions.assertNotEquals(rawId, actual.getId());
    Assertions.assertNotEquals(menuIdFromRaw, actual.getLaunchMenu().getId());

    service.delete(id);

  }

  @Test
  void getRestaurantDtoByIdShouldThrowNotFoundException(){

    Assertions.assertThrows(NotFoundException.class, () -> service.getRestaurantDtoById(rawId));

  }

  // CREATE
  @Test
  void createFromRestaurantDtoTestSuccess() {

    service.createFromRestaurantDto(raw);

    var actual = service.getAll().stream().findFirst().orElse(null);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertEquals(raw.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(raw.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(raw.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());
    Assertions.assertEquals(LocalDate.now(), actual.getUpdateMenuDate());

    Assertions.assertNotEquals(rawId, actual.getId());
    Assertions.assertNotEquals(menuIdFromRaw, actual.getLaunchMenu().getId());

    service.delete(actual.getId());
  }

  @Test
  void createFromRestaurantDtoTestShouldThrowConflictException() {

    var id = service.createFromRestaurantDto(raw).getId();

    Assertions.assertThrows(ConflictException.class, () -> service.createFromRestaurantDto(raw));

    service.delete(id);
  }

  // UPDATE
  @Test
  void updateByIdFromRestaurantDtoSuccess() {

    var menu = raw.getLaunchMenu();

    raw.setLaunchMenu(null);
    var id = service.createFromRestaurantDto(raw).getId();

    var actual = service.getAll().stream().findFirst().orElse(null);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertNull(actual.getLaunchMenu());
    Assertions.assertNull(actual.getUpdateMenuDate());

    raw.setName("newName");
    raw.setLaunchMenu(menu);
    service.updateByIdFromRestaurantDto(id, raw);

    actual = service.getAll().stream().findFirst().orElse(null);

    Assertions.assertNotNull(actual);

    Assertions.assertEquals(raw.getName(), actual.getName());
    Assertions.assertEquals(raw.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(raw.getLaunchMenu().getName(), actual.getLaunchMenu().getName());
    Assertions.assertEquals(raw.getLaunchMenu().getItemList(), actual.getLaunchMenu().getItemList());
    Assertions.assertEquals(LocalDate.now(), actual.getUpdateMenuDate());

    service.delete(id);

  }

  @Test
  void updateByIdFromRestaurantDtoShouldThrowNotFoundException() {

    Assertions.assertThrows(NotFoundException.class, () -> service.updateByIdFromRestaurantDto(rawId, raw));

  }

  @Test
  void updateByIdFromRestaurantDtoShouldThrowConflictException() {

    var firstName = raw.getName();
    var firstId = service.createFromRestaurantDto(raw).getId();

    raw.setName("newSecondName");
    var secondId = service.createFromRestaurantDto(raw).getId();

    raw.setName(firstName);
    Assertions.assertThrows(ConflictException.class, () -> service.updateByIdFromRestaurantDto(secondId, raw));

    service.delete(firstId);
    service.delete(secondId);

  }

}
