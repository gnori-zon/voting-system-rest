package org.gnori.votingsystemrest.service.impl;

import org.gnori.votingsystemrest.dao.impl.MenuDao;
import org.gnori.votingsystemrest.model.entity.MenuEntity;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class MenuService extends AbstractService<MenuEntity, MenuDao> {

  public MenuService(MenuDao dao) {
    super(dao);
  }

  public boolean isExistById(Integer id) {
    return dao.existsById(id);
  }

}
