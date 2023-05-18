package org.gnori.votingsystemrest.factory;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gnori.votingsystemrest.model.dto.MenuDto;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuFactory {

  public MenuDto createMenuDtoFrom(MenuEntity menuEntity) {
    if(menuEntity == null) return null;
    return MenuDto.builder().id(menuEntity.getId())
        .name(menuEntity.getName())
        .itemList(menuEntity.getItemList())
        .build();
  }

  public List<MenuDto> createMenuDtoListFrom(List<MenuEntity> menuEntityList) {
    return menuEntityList.stream()
        .map(this::createMenuDtoFrom)
        .toList();
  }

  public MenuEntity createMenuFrom(MenuDto menuDto) {
    if(menuDto == null) return null;
    return MenuEntity.builder()
        .name(menuDto.getName())
        .itemList(menuDto.getItemList())
        .build();
  }
}
