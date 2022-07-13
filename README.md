# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 3차 PR TODO List

- [x] Line 의 edit 메서드 안의 디버깅 코드 삭제
- [x] Line 의 isOwnDownStation 메서드 이름에 대하여 다시한번 생각해보기, hasStation or contains 과 같은
- [x] Sections 의 size 검증 부분을 isEmpty()로 변경하기
- [x] Sections 의 사용하지 않는 메서드 제거
- [ ] Sections 에서 불변 리스트를 반환하기
- [ ] 에러 메시지를 별도의 클래스로 모아서 사용하는 이유 고민해보기
- [ ] SectionAcceptanceTest 에 stationList.get(2) 의 매직넘버의 의미가 불명확함, 구간이 추가 되었으니 노선 전체의 지하철역을 순서대로 존재하는지 검증
- [ ] 인수 테스트 에서 중복되는 성수역_생성() 에 대하여 고민해보기
- [ ] 인수 테스트 에서 예외 메시지 까지 검증해보기
- [ ] 인수 테스트 에서 Then에 대하여 다시 고민하기
- [ ] 삭제 인수 테스트 리펙토링 하기

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
- [x] http status code 검증 부분도 assertAll() 내부로 이동
- [x] TestFixture 에 대하여 공부하고, 특정 역을 가진 노선을 명확하게 명시하는 메서드 만들기
