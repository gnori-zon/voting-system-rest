package org.gnori.votingsystemrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.controller.constant.Endpoint;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "bearerAuth")
public class MenuController {

  MenuService menuService;

  @Operation(description = "get menu by restaurant id")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = Endpoint.RESTAURANT_MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto getByRestaurantId(@PathVariable Integer restaurantId){

    return menuService.getMenuDtoByRestaurantId(restaurantId);

  }

  @Operation(description = "get all menu")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = Endpoint.MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<MenuDto> getAll() {

    return menuService.getAllMenuDto();

  }

  @Operation(description = "create menu for restaurant")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PostMapping(value = Endpoint.RESTAURANT_MENU_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto createForRestaurant(
      @PathVariable Integer restaurantId,
      @Validated @RequestBody MenuDto menuDto) {

    return menuService.createByRestaurantIdFromMenuDto(restaurantId, menuDto);

  }

  @Operation(description = "update menu for restaurant by ids")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = Endpoint.RESTAURANT_MENU_URL,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto updateFromRestaurant(
      @PathVariable Integer restaurantId,
      @Validated  @RequestBody MenuDto menuDto) {

    return menuService.updateByRestaurantIdFromMenuDto(restaurantId, menuDto);

  }

  @Operation(description = "delete current menu for restaurant")
  @LogMethodExecutionTime
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(Endpoint.RESTAURANT_MENU_URL)
  public void deleteForRestaurant(@PathVariable Integer restaurantId) {

    menuService.deleteByRestaurantId(restaurantId);

  }

}
