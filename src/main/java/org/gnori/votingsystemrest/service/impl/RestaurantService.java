package org.gnori.votingsystemrest.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.error.exceptions.impl.ConflictException;
import org.gnori.votingsystemrest.error.exceptions.impl.NotFoundException;
import org.gnori.votingsystemrest.converter.impl.RestaurantConverter;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
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
@CacheConfig(cacheNames = "restaurant")
public class RestaurantService extends AbstractService<RestaurantEntity, RestaurantDao> {

  private final RestaurantConverter restaurantConverter;
  private final MenuDao menuDao;

  public RestaurantService(
      MenuDao menuDao,
      RestaurantDao dao,
      RestaurantConverter restaurantConverter) {

    super(dao);
    this.restaurantConverter = restaurantConverter;
    this.menuDao = menuDao;

  }

  @Cacheable
  public RestaurantDto getRestaurantDtoById(Integer restaurantId) {

    var restaurantEntity = getAndValidateById(restaurantId);

    return restaurantConverter.convertFrom(restaurantEntity);

  }


  public List<RestaurantDto> getAllRestaurantDto() {

    return restaurantConverter.convertListFrom(getAll());

  }

  public RestaurantDto createFromRestaurantDto(RestaurantDto restaurantDto) {

    checkNameForUniqueness("", restaurantDto.getName());

    var restaurantEntity = restaurantConverter.convertFrom(restaurantDto);

    if (restaurantEntity.getLaunchMenu() != null) {
      restaurantEntity.setUpdateMenuDate(LocalDate.now());
    }

    restaurantEntity = create(restaurantEntity);
    restaurantDto.setId(restaurantEntity.getId());

    if (restaurantEntity.getLaunchMenu() != null) {
      restaurantDto.getLaunchMenu().setId(restaurantEntity.getLaunchMenu().getId());
    }

    return restaurantDto;

  }

  @CachePut(key = "#restaurantId")
  public RestaurantDto updateByIdFromRestaurantDto(
      Integer restaurantId,
      RestaurantDto restaurantDto) {

    var newRestaurantEntity = restaurantConverter.convertFrom(restaurantDto);
    var oldRestaurantEntity = getAndValidateById(restaurantId);

    checkNameForUniqueness(oldRestaurantEntity.getName(), newRestaurantEntity.getName());

    var oldMenu = oldRestaurantEntity.getLaunchMenu();
    var newMenu = newRestaurantEntity.getLaunchMenu();

    if (isMenuUpdate(oldMenu, newMenu)) {

      newRestaurantEntity.setUpdateMenuDate(LocalDate.now());
    }

    restaurantDto = update(restaurantId, newRestaurantEntity)
        .map(restaurantConverter::convertFrom)
        .orElse(null);

    if (restaurantDto != null) clearOldMenuIfExist(oldMenu, restaurantDto.getLaunchMenu());

    return restaurantDto;

  }

  @CacheEvict
  @Override
  public void delete(Integer restaurantId) {
    super.delete(restaurantId);
  }

  private boolean isExistsByName(String name) {

    return dao.existsByName(name);

  }

  private RestaurantEntity getAndValidateById(Integer restaurantId) {

    return get(restaurantId).orElseThrow(
        () -> new NotFoundException(
            String.format("restaurant with id: %d is not exist", restaurantId),
            HttpStatus.NOT_FOUND
        )
    );

  }

  private void checkNameForUniqueness(String oldName, String newName) {


    if (!oldName.equals(newName) && isExistsByName(newName)) {

      throw new ConflictException(
          String.format("restaurant with name: %s is already exist", newName),
          HttpStatus.CONFLICT
      );

    }

  }

  private boolean isMenuUpdate(MenuEntity oldMenu, MenuEntity newMenu) {

    return ((oldMenu == null && newMenu != null) ||
        (oldMenu != null && newMenu == null) ||
        (oldMenu != null && !oldMenu.getItemList().equals(newMenu.getItemList()))
    );

  }


  private void clearOldMenuIfExist(MenuEntity oldMenu, MenuDto newMenu) {

    if (oldMenu != null && newMenu != null && !newMenu.getId().equals(oldMenu.getId())) {
      menuDao.deleteById(oldMenu.getId()); // deleted if menu not bind
    }

  }

}
