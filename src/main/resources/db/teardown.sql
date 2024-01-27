SET REFERENTIAL_INTEGRITY FALSE; -- 모든 제약조건 비활성화
truncate table station;
truncate table line;
SET REFERENTIAL_INTEGRITY TRUE; -- 모든 제약조건 활성화
insert into station (id, name) values (1, '부천역');
insert into station (id, name) values (2, '선릉역');
insert into station (id, name) values (3, '삼성역');