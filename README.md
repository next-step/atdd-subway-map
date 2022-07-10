# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 2차 PR TODO List

- [x] LineResponse 내부의 List<Station> 수정
- [x] toDomain 메서드에 대하여 다시 생각해보기
- [x] LineService 내부의 findAll, findByIs 에서 중복부분 제거
- [x] Line의 기본생성자 접근제어자 수정
- [x] 인수테스트 부분에서 assertAll로 변경, containsExactly 활용
- [x] editLine 테스트 에서 이름과 색상이 변경되었는지 검증하도록 변경
- [x] 지하철 노선 생성 메서드에서 upStationId, downStationId, 10 에 대한 중복 제거하기

추가 수정 사항

- [x] 통일성을 위해 @ResponseStatus 보다는 내용(body)없이 응답 코드만 반환하기
- [x] LineService.findAllStationInLine 내부 메서드 레퍼런스로 변경
- [x] LineAcceptanceTest에서 getLong()은 primitive type을 반환한다. 이에 알맞은 검증코드로 변경하기
- [ ] http status code 검증 부분도 assertAll() 내부로 이동
- [ ] TestFixture 에 대하여 공부하고, 특정 역을 가진 노선을 명확하게 명시하는 메서드 만링기
