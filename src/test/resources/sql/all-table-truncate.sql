SET REFERENTIAL_INTEGRITY FALSE; --제약조건 무효화
truncate table `section`;
truncate table `line`;
truncate table `station`;
SET REFERENTIAL_INTEGRITY TRUE; --제약조건 무효화
