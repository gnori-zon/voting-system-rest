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

    var menuEntity = restaurantDao.findById(restaurantId)
        .orElseThrow(
            () -> new NotFoundException(
                String.format("restaurant with id: %d is not exist", restaurantId),
                HttpStatus.NOT_FOUND)
        ) // if restaurant is not exists
        .getLaunchMenu();

    if (menuEntity == null) {

      throw  new NotFoundException(
          String.format("menu from restaurant with id: %d is not exist", restaurantId),
          HttpStatus.NOT_FOUND);

    }

    return menuConverter.convertFrom(menuEntity);

  }

  public List<MenuDto> getAllMenuDto() {

    return menuConverter.convertListFrom(getAll());

  }

  @CachePut(key = "#restaurantId")
  public MenuDto createByRestaurantIdFromMenuDto(Integer restaurantId, MenuDto menuDto) {

    return restaurantDao.findById(restaurantId).map(
        restaurantEntity -> {

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
    ).orElseThrow(
        () -> new NotFoundException(
            String.format("restaurant with id: %d is not exist", restaurantId),
            HttpStatus.NOT_FOUND)
    ); // if restaurant is not exists

  }

  @CachePut(key = "#restaurantId")
  public MenuDto updateByRestaurantIdFromMenuDto(Integer restaurantId, MenuDto menuDto) {

    return restaurantDao.findById(restaurantId).map(
        restaurantEntity -> {

          var menuEntity = menuConverter.convertFrom(menuDto);
          restaurantEntity.setUpdateMenuDate(LocalDate.now());

          if (restaurantDao.isExistsMenu(restaurantId)) {
            update(restaurantEntity.getLaunchMenu().getId(), menuEntity);
          } else {
            restaurantEntity.setLaunchMenu(menuEntity);
            restaurantDao.save(restaurantEntity);
          }

          menuDto.setId(restaurantEntity.getLaunchMenu().getId());

          return menuDto;
        }
    ).orElseThrow(
        () -> new NotFoundException(
            String.format("restaurant with id: %d is not exist", restaurantId),
            HttpStatus.NOT_FOUND)
    ); // if restaurant is not exists

  }

  @CacheEvict
  public void deleteByRestaurantId(Integer restaurantId) {

    restaurantDao.findById(restaurantId).ifPresent(
        restaurantEntity -> {

          var menuEntity = restaurantEntity.getLaunchMenu();

          restaurantEntity.setUpdateMenuDate(null);
          restaurantEntity.setLaunchMenu(null);
          restaurantDao.save(restaurantEntity);

          if (menuEntity != null) {
            delete(menuEntity.getId());
          }

        }
    );

  }
}
