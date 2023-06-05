package org.gnori.votingsystemrest.converter.impl;

import java.util.List;
import org.gnori.votingsystemrest.converter.BaseConverter;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuConverter implements BaseConverter<MenuDto, MenuEntity> {

  public MenuDto convertFrom(MenuEntity menuEntity) {

    if(menuEntity == null) return null;

    return MenuDto.builder().id(menuEntity.getId())
        .name(menuEntity.getName())
        .itemList(menuEntity.getItemList())
        .build();

  }

  public List<MenuDto> convertListFrom(List<MenuEntity> menuEntityList) {

    return menuEntityList.stream()
        .map(this::convertFrom)
        .toList();

  }

  public MenuEntity convertFrom(MenuDto menuDto) {

    if(menuDto == null) return null;

    return MenuEntity.builder()
        .name(menuDto.getName())
        .itemList(menuDto.getItemList())
        .build();

  }
}
