package org.gnori.votingsystemrest.service.impl;

import org.gnori.votingsystemrest.dao.impl.UserDao;
import org.gnori.votingsystemrest.model.entity.UserEntity;
import org.gnori.votingsystemrest.service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<UserEntity, UserDao> {

  public UserService(UserDao dao) {
    super(dao);
  }

}
