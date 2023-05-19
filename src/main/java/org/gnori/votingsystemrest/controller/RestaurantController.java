package org.gnori.votingsystemrest.controller;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
import org.gnori.votingsystemrest.service.impl.RestaurantService;
import org.springframework.http.HttpStatus;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RestaurantController {

  public static final String RESTAURANT_URL = "/restaurants";
  public static final String RESTAURANT_URL_WITH_ID = RESTAURANT_URL+"/{id}";

  private final RestaurantService restaurantService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(RESTAURANT_URL_WITH_ID)
  public RestaurantDto getById(@PathVariable Integer id) {

    return restaurantService.getRestaurantDtoById(id);

  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(RESTAURANT_URL)
  public List<RestaurantDto> getAll() {

    return restaurantService.getAllRestaurantDto();

  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(RESTAURANT_URL)
  public RestaurantDto create(@Validated @RequestBody RestaurantDto restaurantDto) {

    return restaurantService.createFromRestaurantDto(restaurantDto);

  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(RESTAURANT_URL_WITH_ID)
  public RestaurantDto update(@PathVariable Integer id, @Validated @RequestBody RestaurantDto restaurantDto) {

    return restaurantService.updateByIdFromRestaurantDto(id, restaurantDto);

  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping(RESTAURANT_URL_WITH_ID)
  public void delete(@PathVariable Integer id) {

    restaurantService.delete(id);

  }
}
