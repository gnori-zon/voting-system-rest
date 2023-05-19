package org.gnori.votingsystemrest.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.error.ConflictException;
import org.gnori.votingsystemrest.error.NotFoundException;
import org.gnori.votingsystemrest.factory.MenuFactory;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MenuService extends AbstractService<MenuEntity, MenuDao> {

  private final RestaurantDao restaurantDao;
  private final MenuFactory menuFactory;

  public MenuService(MenuDao dao, RestaurantDao restaurantDao, MenuFactory menuFactory) {
    super(dao);
    this.restaurantDao = restaurantDao;
    this.menuFactory = menuFactory;
  }

  public MenuDto getMenuDtoByRestaurantId(Integer restaurantId) {

    var menu = restaurantDao.findById(restaurantId)
        .orElseThrow(
            () -> new NotFoundException(
                String.format("restaurant with id: %d is not exist", restaurantId),
                HttpStatus.NOT_FOUND)
        ) // if restaurant is not exists
        .getLaunchMenu();

    if (menu == null) {
      throw  new NotFoundException(
          String.format("menu from restaurant with id: %d is not exist", restaurantId),
          HttpStatus.NOT_FOUND);
    }

    return menuFactory.convertFrom(menu);

  }

  public List<MenuDto> getAllMenuDto() {
    return menuFactory.convertListFrom(getAll());
  }

  public MenuDto createForRestaurant(Integer restaurantId, MenuDto menuDto) {

    return restaurantDao.findById(restaurantId).map(
        restaurantEntity -> {

          if (restaurantDao.isExistsMenu(restaurantId)) {
            throw new ConflictException(
                String.format("menu from restaurant with id: %d is already exist", restaurantId),
                HttpStatus.CONFLICT);
          }

          var menu = create(menuFactory.convertFrom(menuDto));
          restaurantEntity.setLaunchMenu(menu);
          restaurantEntity.setUpdateMenuDate(LocalDate.now());
          restaurantDao.save(restaurantEntity);

          menuDto.setId(menu.getId());
          return menuDto;
        }
    ).orElseThrow(
        () -> new NotFoundException(
            String.format("restaurant with id: %d is not exist", restaurantId),
            HttpStatus.NOT_FOUND)
    ); // if restaurant is not exists

  }

  public MenuDto updateByRestaurantIdFromMenuDto(Integer restaurantId, MenuDto menuDto) {

    return restaurantDao.findById(restaurantId).map(
        restaurantEntity -> {
          var menuEntity = menuFactory.convertFrom(menuDto);
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

  public void deleteByRestaurantId(Integer restaurantId) {

    restaurantDao.findById(restaurantId).ifPresent(
        restaurantEntity -> {
          var menu = restaurantEntity.getLaunchMenu();
          restaurantEntity.setUpdateMenuDate(null);
          restaurantEntity.setLaunchMenu(null);
          restaurantDao.save(restaurantEntity);
          if (menu != null) {
            delete(menu.getId());
          }
        }
    );

  }
}
