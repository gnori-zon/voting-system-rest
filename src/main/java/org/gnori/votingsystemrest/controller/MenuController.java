package org.gnori.votingsystemrest.controller;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.MenuFactory;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.service.impl.MenuService;
import org.gnori.votingsystemrest.service.impl.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MenuController {

  public static final String RESTAURANT_MENU_URL = "/restaurants/{restaurantId}/menu";
  public static final String MENU_URL = "/menues";
  public static final String MENU_URL_WITH_ID = MENU_URL+"/{menuId}";

  MenuService menuService;
  RestaurantService restaurantService;
  MenuFactory menuFactory;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = RESTAURANT_MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto getMenuByRestaurantId(@PathVariable Integer restaurantId){

    var menu = restaurantService.get(restaurantId)
        .orElseThrow(
            () ->new NotFoundException(String.format("restaurant with id: %d is not exist", restaurantId),
                HttpStatus.NOT_FOUND)
        ) // if restaurant is not exists
        .getLaunchMenu();

    return menuFactory.createMenuDtoFrom(menu);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<MenuDto> getAll() {

    return menuFactory.createMenuDtoListFrom(menuService.getAll());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = MENU_URL_WITH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto getMenuById(@PathVariable Integer menuId) {

    var optionalMenu = menuService.get(menuId).orElse(null);

    return menuFactory.createMenuDtoFrom(optionalMenu);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = MENU_URL,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto create(@RequestBody MenuDto menuDto) {

    var menuEntity = menuService.create(menuFactory.createMenuFrom(menuDto));
    menuDto.setId(menuEntity.getId());
    return menuDto;
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = MENU_URL_WITH_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto updateMenu(@PathVariable Integer menuId, @RequestBody MenuDto menuDto) {

    menuService.update(menuId, menuFactory.createMenuFrom(menuDto));

    return menuDto;
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = RESTAURANT_MENU_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto updateMenuFromRestaurant(@PathVariable Integer restaurantId, @RequestBody MenuDto menuDto) {

    return restaurantService.get(restaurantId).map(
        restaurantEntity -> {
          var menuEntity = menuFactory.createMenuFrom(menuDto);
          if (restaurantService.isExistMenu(restaurantId)) {
            menuService.update(restaurantEntity.getLaunchMenu().getId(), menuEntity);
            menuDto.setId(restaurantEntity.getLaunchMenu().getId());
          } else {
            menuEntity = menuService.create(menuEntity);
            restaurantEntity.setLaunchMenu(menuEntity);
            restaurantService.update(restaurantId, restaurantEntity);
            menuDto.setId(menuEntity.getId());
          }
          return menuDto;
        }
    ).orElseThrow(() ->new NotFoundException(String.format("restaurant with id: %d is not exist", restaurantId), HttpStatus.NOT_FOUND)); // if restaurant is not exists
  }
//  // TODO move to restaurantController, because updates restaurant state
//  @ResponseStatus(HttpStatus.OK)
//  @PutMapping(value = RESTAURANT_MENU_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//  public MenuDto updateMenuForRestaurant(@PathVariable Integer restaurantId, @RequestBody MenuDto menuDto) {
//
//    return restaurantService.get(restaurantId).map(
//        restaurantEntity -> {
//          var menu = menuService.create(menuFactory.createMenuFrom(menuDto));
//          restaurantEntity.setLaunchMenu(menu);
//          restaurantService.update(restaurantId, restaurantEntity);
//
//          menuDto.setId(menu.getId());
//          return menuDto;
//        }
//    ).orElseThrow(
//        () ->new NotFoundException(String.format("restaurant with id: %d is not exist", restaurantId),
//            HttpStatus.NOT_FOUND)
//    ); // if restaurant is not exists
//  }
  // TODO move to restaurantController, because updates restaurant state
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = RESTAURANT_MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
  public MenuDto updateMenuForRestaurant(@PathVariable Integer restaurantId, @RequestParam Integer menuId) {

    return restaurantService.get(restaurantId).map(
        restaurantEntity -> menuService.get(menuId).map(
            menuEntity -> {
              restaurantEntity.setLaunchMenu(menuEntity);
              restaurantService.update(restaurantId, restaurantEntity);
              return menuFactory.createMenuDtoFrom(menuEntity);
            }
        ).orElseThrow(
            () ->new NotFoundException(String.format("menu with id: %d is not exist", menuId),
                HttpStatus.NOT_FOUND)
        ) // if menu is not exists
    ).orElseThrow(
        () ->new NotFoundException(String.format("restaurant with id: %d is not exist", restaurantId),
            HttpStatus.NOT_FOUND)
    ); // if restaurant is not exists
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(value = MENU_URL_WITH_ID)
  public void deleteMenuById(@PathVariable Integer menuId) {
    menuService.delete(menuId);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(value = RESTAURANT_MENU_URL)
  public void deleteMenuForRestaurant(@PathVariable Integer restaurantId) {
    restaurantService.get(restaurantId).ifPresent(
        restaurantEntity -> menuService.delete(restaurantEntity.getLaunchMenu().getId())
    );
  }

}
