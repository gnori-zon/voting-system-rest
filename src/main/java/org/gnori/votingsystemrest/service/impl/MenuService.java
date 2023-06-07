package org.gnori.votingsystemrest.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.error.exceptions.impl.ConflictException;
import org.gnori.votingsystemrest.error.exceptions.impl.NotFoundException;
import org.gnori.votingsystemrest.converter.impl.MenuConverter;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "menu")
public class MenuService extends AbstractService<MenuEntity, MenuDao> {

  private final RestaurantDao restaurantDao;
  private final MenuConverter menuConverter;

  public MenuService(MenuDao dao, RestaurantDao restaurantDao, MenuConverter menuConverter) {
    super(dao);
    this.restaurantDao = restaurantDao;
    this.menuConverter = menuConverter;
  }

  @Cacheable
  public MenuDto getMenuDtoByRestaurantId(Integer restaurantId) {

    var menuEntity = getAndValidateByRestaurantId(restaurantId);

    return menuConverter.convertFrom(menuEntity);

  }

  public List<MenuDto> getAllMenuDto() {

    return menuConverter.convertListFrom(getAll());

  }

  @CachePut(key = "#restaurantId")
  public MenuDto createByRestaurantIdFromMenuDto(Integer restaurantId, MenuDto menuDto) {

    var restaurantEntity = getAndValidateRestaurantById(restaurantId);

    if (restaurantDao.isExistsMenu(restaurantId)) {

      throw new ConflictException(
          String.format("menu from restaurant with id: %d is already exist", restaurantId),
          HttpStatus.CONFLICT);
    }

    var menuEntity = create(menuConverter.convertFrom(menuDto));

    restaurantEntity.setLaunchMenu(menuEntity);
    restaurantEntity.setUpdateMenuDate(LocalDate.now());

    restaurantDao.save(restaurantEntity);

    menuDto.setId(menuEntity.getId());
    return menuDto;

  }

  @CachePut(key = "#restaurantId")
  public MenuDto updateByRestaurantIdFromMenuDto(Integer restaurantId, MenuDto menuDto) {

    var restaurantEntity = getAndValidateRestaurantById(restaurantId);

    var menuEntity = menuConverter.convertFrom(menuDto);

    if (restaurantDao.isExistsMenu(restaurantId)) {
      update(restaurantEntity.getLaunchMenu().getId(), menuEntity);
    } else {
      restaurantEntity.setLaunchMenu(menuEntity);
      restaurantDao.save(restaurantEntity);
    }
    restaurantEntity.setUpdateMenuDate(LocalDate.now());

    menuDto.setId(restaurantEntity.getLaunchMenu().getId());
    return menuDto;

  }

  @CacheEvict
  public void deleteByRestaurantId(Integer restaurantId) {

    var restaurant = getAndValidateRestaurantById(restaurantId);

    var menuEntity = restaurant.getLaunchMenu();

    restaurant.setUpdateMenuDate(null);
    restaurant.setLaunchMenu(null);
    restaurantDao.save(restaurant);

    if (menuEntity != null) {
      delete(menuEntity.getId());
    }

  }

  private MenuEntity getAndValidateByRestaurantId(Integer restaurantId) {

    var restaurant = getAndValidateRestaurantById(restaurantId);

    if (restaurant.getLaunchMenu() == null) {

      throw  new NotFoundException(
          String.format("menu from restaurant with id: %d is not exist", restaurantId),
          HttpStatus.NOT_FOUND);

    }

    return restaurant.getLaunchMenu();

  }

  private RestaurantEntity getAndValidateRestaurantById(Integer restaurantId) {

    return restaurantDao.findById(restaurantId)
        .orElseThrow(
            () -> new NotFoundException(
                String.format("restaurant with id: %d is not exist", restaurantId),
                HttpStatus.NOT_FOUND)
        ); // if restaurant is not exists

  }
}
