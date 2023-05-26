package org.gnori.votingsystemrest.factory;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantFactory implements BaseFactory<RestaurantDto, RestaurantEntity> {

  private final MenuFactory menuFactory;

  public RestaurantDto convertFrom(RestaurantEntity restaurantEntity) {

    if (restaurantEntity == null) return null;

    return RestaurantDto.builder()
        .id(restaurantEntity.getId())
        .name(restaurantEntity.getName())
        .launchMenu(menuFactory.convertFrom(restaurantEntity.getLaunchMenu()))
        .build();

  }

  public List<RestaurantDto> convertListFrom(List<RestaurantEntity> restaurantEntityList) {

    return restaurantEntityList.stream()
        .map(this::convertFrom)
        .toList();

  }

  public RestaurantEntity convertFrom(RestaurantDto restaurantDto) {

    if (restaurantDto == null) return null;

    return RestaurantEntity.builder()
        .name(restaurantDto.getName())
        .launchMenu(menuFactory.convertFrom(restaurantDto.getLaunchMenu()))
        .build();

  }

}
