package org.gnori.votingsystemrest.controller;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.log.annotation.LogMethodExecutionTime;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.service.impl.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MenuController {

  public static final String RESTAURANT_MENU_URL = "/restaurants/{restaurantId}/menu";
  public static final String MENU_URL = "/menues";

  MenuService menuService;

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = RESTAURANT_MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto getByRestaurantId(@PathVariable Integer restaurantId){

    return menuService.getMenuDtoByRestaurantId(restaurantId);

  }

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<MenuDto> getAll() {

    return menuService.getAllMenuDto();

  }

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PostMapping(value = RESTAURANT_MENU_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto createForRestaurant(
      @PathVariable Integer restaurantId,
      @Validated @RequestBody MenuDto menuDto) {

    return menuService.createByRestaurantIdFromMenuDto(restaurantId, menuDto);

  }

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = RESTAURANT_MENU_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto updateFromRestaurant(
      @PathVariable Integer restaurantId,
      @Validated  @RequestBody MenuDto menuDto) {

    return menuService.updateByRestaurantIdFromMenuDto(restaurantId, menuDto);

  }

  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(RESTAURANT_MENU_URL)
  public void deleteForRestaurant(@PathVariable Integer restaurantId) {

    menuService.deleteByRestaurantId(restaurantId);

  }

}
