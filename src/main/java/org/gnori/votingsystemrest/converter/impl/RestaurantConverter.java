package org.gnori.votingsystemrest.converter.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.converter.BaseConverter;
import org.gnori.votingsystemrest.model.dto.RestaurantDto;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantConverter implements BaseConverter<RestaurantDto, RestaurantEntity> {

  private final MenuConverter menuConverter;

  public RestaurantDto convertFrom(RestaurantEntity restaurantEntity) {

    if (restaurantEntity == null) return null;

    return RestaurantDto.builder()
        .id(restaurantEntity.getId())
        .name(restaurantEntity.getName())
        .launchMenu(menuConverter.convertFrom(restaurantEntity.getLaunchMenu()))
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
        .launchMenu(menuConverter.convertFrom(restaurantDto.getLaunchMenu()))
        .build();

  }

}
