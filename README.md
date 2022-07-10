# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 2차 PR TODO List

- [x] LineResponse 내부의 List<Station> 수정
- [ ] toDomain 메서드에 대하여 다시 생각해보기
- [x] LineService 내부의 findAll, findByIs 에서 중복부분 제거
- [x] Line의 기본생성자 접근제어자 수정
- [ ] 인수테스트 부분에서 assertAll로 변경, containsExactly 활용
- [ ] editLine 테스트 에서 이름과 색상이 변경되었는지 검증하도록 변경
- [ ] 지하철 노선 생성 메서드에서 upStationId, downStationId, 10 에 대한 중복 제거하기
