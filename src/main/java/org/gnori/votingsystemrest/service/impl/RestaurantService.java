package org.gnori.votingsystemrest.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.error.ConflictException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.RestaurantFactory;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService extends AbstractService<RestaurantEntity, RestaurantDao> {

  private final RestaurantFactory restaurantFactory;
  private final MenuDao menuDao;

  public RestaurantService(
      MenuDao menuDao,
      RestaurantDao dao,
      RestaurantFactory restaurantFactory) {

    super(dao);
    this.restaurantFactory = restaurantFactory;
    this.menuDao = menuDao;

  }

  public boolean isExistsByName(String name) {

    return dao.existsByName(name);

  }

  public RestaurantDto getRestaurantDtoById(Integer id) {

    var restaurant = get(id).orElseThrow(
        () -> new NotFoundException(String.format("restaurant with id: %d is not exist", id),
            HttpStatus.NOT_FOUND)
    );

    return restaurantFactory.convertFrom(restaurant);

  }


  public List<RestaurantDto> getAllRestaurantDto() {

    return restaurantFactory.convertListFrom(getAll());

  }

  public RestaurantDto createFromRestaurantDto(RestaurantDto restaurantDto) {

    if (isExistsByName(restaurantDto.getName())) {

      throw new ConflictException(
          String.format("restaurant with name: %s is already exist", restaurantDto.getName()),
          HttpStatus.CONFLICT
      );

    }

    var restaurant = restaurantFactory.convertFrom(restaurantDto);

    if (restaurant.getLaunchMenu() != null) {
      restaurant.setUpdateMenuDate(LocalDate.now());
    }

    restaurant = create(restaurant);
    restaurantDto.setId(restaurant.getId());

    if (restaurant.getLaunchMenu() != null) {
      restaurantDto.getLaunchMenu().setId(restaurant.getLaunchMenu().getId());
    }

    return restaurantDto;

  }

  public RestaurantDto updateByIdFromRestaurantDto(Integer id, RestaurantDto restaurantDto) {

    var newRestaurant = restaurantFactory.convertFrom(restaurantDto);
    var oldRestaurant = get(id).orElseThrow(
        () -> new NotFoundException(
            String.format("restaurant with id: %d is not exist", id),
            HttpStatus.NOT_FOUND
        )
    );

    if (!oldRestaurant.getName().equals(newRestaurant.getName()) &&
        isExistsByName(newRestaurant.getName())) {

      throw new ConflictException(
          String.format("restaurant with name: %s is already exist", restaurantDto.getName()),
          HttpStatus.CONFLICT
      );

    }

    var oldMenu = oldRestaurant.getLaunchMenu();
    var newMenu = newRestaurant.getLaunchMenu();

    if ((oldMenu == null && newMenu != null) ||
        (oldMenu != null && newMenu == null) ||
        (oldMenu != null && !oldRestaurant.getLaunchMenu().getItemList().equals(
            newRestaurant.getLaunchMenu().getItemList()
        ))) {

      newRestaurant.setUpdateMenuDate(LocalDate.now());
    }

    restaurantDto = update(id, newRestaurant)
        .map(restaurantFactory::convertFrom)
        .orElse(null);

    if (oldMenu != null)  menuDao.deleteById(oldMenu.getId()); // deleted if menu not bind

    return restaurantDto;

  }

}
