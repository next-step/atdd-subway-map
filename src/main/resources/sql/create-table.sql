create table line (
                      line_id bigint generated by default as identity,
                      line_name varchar(255),
                      color varchar(255),
                      last_down_station_id bigint,
                      last_up_station_id bigint,
                      distance integer,
                      primary key (line_id)
);

create table station (
                         station_id bigint generated by default as identity,
                         line_id bigint,
                         station_name varchar(20) not null,
                         primary key (station_id)
);