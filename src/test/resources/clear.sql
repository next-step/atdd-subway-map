EXECUTE IMMEDIATE 'TRUNCATE TABLE station'
EXECUTE IMMEDIATE 'TRUNCATE TABLE line'


-- 아래는 시도했던 내용들 기록용으로 남겨두겠습니다! --

-- 테이블 이름을 조회하는 법
-- SELECT
--     Concat('TRUNCATE TABLE ', TABLE_NAME, ';')
-- FROM
--     INFORMATION_SCHEMA.TABLES
-- WHERE
--     TABLE_SCHEMA = 'PUBLIC';



-- mysql 에서만 가능한 PREPARE 구문
-- SET FOREIGN_KEY_CHECKS = 0;
-- SELECT @str := CONCATConcat('TRUNCATE TABLE ', TABLE_NAME, ';')
-- FROM INFORMATION_SCHEMA.TABLES
-- WHERE TABLE_SCHEMA = 'PUBLIC';
-- PREPARE stmt FROM @str;
-- EXECUTE stmt;
-- DEALLOCATE PREPARE stmt;
-- SET FOREIGN_KEY_CHECKS = 1;



-- single quote에 의해 원하는 대로 실행되지 않음
-- SET @query = (SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC');
-- EXECUTE IMMEDIATE @query



-- Datasource에서 쿼리 실행해서 quote를 얻어오고, 얻어온 quote를 실행하면 성공하긴 함!