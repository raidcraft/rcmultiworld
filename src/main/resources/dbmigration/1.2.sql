-- apply changes
create table rc_multiworld_tp_requests (
  id                            integer auto_increment not null,
  player                        varchar(40),
  world_id                      integer,
  x                             integer not null,
  y                             integer not null,
  z                             integer not null,
  yaw                           integer not null,
  pitch                         integer not null,
  constraint pk_rc_multiworld_tp_requests primary key (id)
);

create table rc_multiworld_world (
  id                            integer auto_increment not null,
  alias                         varchar(255),
  server                        varchar(255),
  world_id                      varchar(40),
  folder                        varchar(255),
  constraint pk_rc_multiworld_world primary key (id)
);

create index ix_rc_multiworld_tp_requests_world_id on rc_multiworld_tp_requests (world_id);
alter table rc_multiworld_tp_requests add constraint fk_rc_multiworld_tp_requests_world_id foreign key (world_id) references rc_multiworld_world (id) on delete restrict on update restrict;

