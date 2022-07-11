truncate table station;
alter table station
    alter column id restart with 1;

truncate table line;
alter table line
    alter column id restart with 1;