set foreign_key_checks = 0;

truncate table line_station;
alter table line_station alter column id restart with 1;

truncate table station;
alter table station alter column id restart with 1;

truncate table line;
alter table line alter column id restart with 1;

set foreign_key_checks = 1;