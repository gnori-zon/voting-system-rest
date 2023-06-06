--liquibase formatted sql

--changeset gnori:1
--comment create table menu
create table menu (
  id bigserial not null primary key,
  name varchar(128) not null,
  item_list jsonb not null
);
-- rollback drop table menu;

--changeset gnori:2
--comment create table restaurant

create table restaurant (
  id bigserial not null primary key,
  name varchar(128) not null unique,
  update_menu_date date
);
-- rollback drop table restaurant;

--changeset gnori:3
--comment binding restaurant with menu
alter table restaurant add column launch_menu_id bigint;
alter table restaurant add constraint FK_restaurant_menu foreign key (launch_menu_id) references menu;
-- rollback alter table restaurant drop constraint FK_restaurant_menu;
-- rollback alter table restaurant drop column launch_menu_id;


--changeset gnori:4
--comment create table users

create table users (
  id bigserial not null primary key,
  username varchar(128) not null unique,
  password varchar(255) not null,
  voted_for bigint,
  date_vote date
);
-- rollback drop table users;

--changeset gnori:5
--comment create table user_role

create table user_role (
                       user_id bigint not null,
                       role varchar(128),
                       constraint uk_user_role unique (user_id, role)
);
-- rollback drop table user_role;

--changeset gnori:6
--comment binding user with user_role
alter table user_role add constraint FK_user_role_user foreign key (user_id) references users on delete cascade;
-- rollback alter table restaurant drop column launch_menu_id;
-- rollback alter table user_role drop constraint FK_user_role_user;