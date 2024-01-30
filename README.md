# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

---

## 🚀 0단계 - 리뷰 사이클 연습

### Todo

- [x] "https://google.com"로 요청을 보낼 경우 200응답 코드가 오는지를 검증하는 테스트를 작성
  - Response 객체로 검증
  - .statusCode()로 검증

---

## 🚀 1단계 - 지하철역 인수 테스트 작성

### Todo

- [x] 지하철역 인수 테스트 완성하기
  - [x] 지하철역 목록 조회 인수 테스트 작성하기
  - [x] 지하철역 삭제 인수 테스트 작성하기

### 프로그래밍 요구사항

- [x] 인수 테스트의 재사용성과 가독성, 빠른 테스트 의도 파악을 위해 인수 테스트 리팩터링

### Feedback 24.01.27
- [x] 지하철역 생성 메서드는 앞으로 자주 사용 예정으로, 함수로 추출해 볼 것

---

## 🚀 2단계 - 지하철 노선 관리

### Todo
- 인수 조건을 기반으로 지하철 노선 관리 기능 구현
- [x] 지하철 노선 생성
- [x] 지하철 노선 목록 조회
- [x] 지하철 노선 조회
- [ ] 지하철 노선 수정
- [ ] 지하철 노선 삭제

#### 인수 조건
- [x] 지하철 노선 생성
  - When 지하철 노선을 생성하면 Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
- [x] 지하철 노선 목록 조회
  - Given 2개의 지하철 노선을 생성하고 When 지하철 노선 목록을 조회하면 Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
- [x] 지하철 노선 조회
  - Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 조회하면 Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
- [ ] 지하철 노선 삭제
  - Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 삭제하면 Then 해당 지하철 노선 정보는 삭제된다.

### 프로그래밍 요구사항
- [ ] 인수 테스트 작성, 인수 테스트 충족하는 기능 구현의 순서로 진행
- [x] 인수 테스트간에 영향을 끼치지 않도록 테스트 격리
- [ ] 인수 테스트 리팩터링 진행 (중복 코드 처리)
