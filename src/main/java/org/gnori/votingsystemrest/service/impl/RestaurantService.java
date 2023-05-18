package org.gnori.votingsystemrest.service.impl;

import org.gnori.votingsystemrest.dao.impl.RestaurantDao;
import org.gnori.votingsystemrest.model.entity.RestaurantEntity;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService extends AbstractService<RestaurantEntity, RestaurantDao> {

  public RestaurantService(RestaurantDao dao) {
    super(dao);
  }

  public boolean isExistMenu(Integer id) {
    return dao.isExistMenu(id);
  }
}
