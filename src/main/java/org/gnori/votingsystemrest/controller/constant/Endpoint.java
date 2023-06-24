package org.gnori.votingsystemrest.controller.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoint {

  public static final String BASE_URL = "/api/v1";
  public static final String FOR_ADMIN = BASE_URL + "/admin";

  public static final String ADMIN_USERS = FOR_ADMIN + "/users";
  public static final String ADMIN_USERS_WITH_ID_URL = ADMIN_USERS + "/{userId}";

  public static final String RESTAURANT_URL = FOR_ADMIN + "/restaurants";
  public static final String RESTAURANT_URL_WITH_ID = RESTAURANT_URL +"/{restaurantId}";

  public static final String RESTAURANT_MENU_URL = RESTAURANT_URL_WITH_ID + "/menu";
  public static final String MENU_URL = FOR_ADMIN + "/menues";

  public static final String USER_URL = BASE_URL + "/users";
  public static final String AUTH_URL = BASE_URL + "/auth";

  public static final String USER_VOTE_URL = USER_URL + "/vote";
  public static final String VOTES_URL = BASE_URL + "/votes";
}
