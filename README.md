# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 1주차 미션
- Review 정리
  - 가변인자를 받는 `containsAnyOf` 메서드 활용
  - 매직넘버를 사용하는 것을 지양할 것
  - 메서드명에 `And`와 같이 2가지 이상의 일을 하고 있음을 표현하는 것을 지양할 것

## 2주차 미션
- 진행 순서
  1. 인수 테스트 작성
  2. 작성한 테스트가 `Pass` 되도록 기능 구현

- 인수 테스트 작성
  - [ ] 지하철 노선 생성
  - [ ] 지하철 노선 목록 조회
  - [ ] 지하철 노선 조회
  - [ ] 지하철 노선 수정
  - [ ] 지하철 노선 삭제

- 인수 테스트 성공
    - [ ] 지하철 노선 생성
    - [ ] 지하철 노선 목록 조회
    - [ ] 지하철 노선 조회
    - [ ] 지하철 노선 수정
    - [ ] 지하철 노선 삭제

- 고민
  1. 테스트 검증 범위를 어디까지 둘 것인가?
     - 예) 지하철 노선명 + 색상?
  2. 미션 진행 순서
     1. 모든 인수 테스트 작성(리팩토링 X)
        - 요청: 로그를 통해 request method, URI, body 정도?
        - 응답: 로그를 통해 응답 상태코드 정도?(실패하는지?)
     2. 인수 테스트 리팩토링
     3. 기능 구현
     4. 기능 리팩토링
     5. 인수 테스트 최종 검토