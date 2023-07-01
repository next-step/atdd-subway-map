drop table line;
drop table station;

create table station (
                      ID int not null AUTO_INCREMENT,
                      NAME varchar(20) not null,
                      PRIMARY KEY ( ID )
);

create table line (
                         ID int not null AUTO_INCREMENT,
                         NAME varchar(20) not null,
                         COLOR varchar(20) not null,
                         up_station_id int not null,
                         down_station_id int not null,
                         distance int not null,
                         PRIMARY KEY ( ID )
);