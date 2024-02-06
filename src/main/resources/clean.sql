SET REFERENTIAL_INTEGRITY FALSE;

-- H2는 TRUNCATE TABLE을 실행하더라도 IDENTITY가 기본값으로 재설정 필요
TRUNCATE TABLE station RESTART IDENTITY;
TRUNCATE TABLE station_line RESTART IDENTITY;
TRUNCATE TABLE station_section RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;