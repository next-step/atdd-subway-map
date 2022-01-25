<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```

---

# Step 1. 노선 관리 기능 구현
인수 테스트 작성과 테스트를 충족하는 기능 구현

### 요구사항
- [X] 지하철 노선 생성 
  - [X] 인수 테스트 작성
    - Scenario: 지하철 노선 생성
    - When 지하철 노선 생성을 요청 하면
    - Then 지하철 노선 생성이 성공한다.
- [X] 지하철 노선 목록 조회
  - [X] 인수 테스트 작성
    - Given
      - 지하철 노선 생성을 요청 하고
      - 새로운 지하철 노선 생성을 요청 하고
    - When 지하철 노선 목록 조회를 요청 하면
    - Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
  - [X] 기능 구현
- [X] 지하철 노선 조회
  - [X] 인수 테스트 작성
    - Given 지하철 노선 생성을 요청 하고
    - When 생성한 지하철 노선 조회를 요청 하면
    - Then 생성한 지하철 노선을 응답받는다
  - [X] 기능 구현
- [X] 지하철 노선 수정
  - [X] 인수 테스트 작성
    - case1: 존재하는 노선을 수정
      - Given 지하철 노선 생성을 요청 하고
      - When 지하철 노선의 정보 수정을 요청 하면
      - Then 지하철 노선의 정보 수정은 성공한다.
    - case2: 존재하지 않는 노선을 수정
      - When 없는 지하철 노선의 정보 수정을 요청하면
      - Then 노선 수정이 실패한다.
  - [X] 기능 구현
- [X] 지하철 노선 삭제
  - [X] 인수 테스트 작성
    - Scenario: 지하철 노선 삭제
    - Given 지하철 노선 생성을 요청 하고
    - When 생성한 지하철 노선 삭제를 요청 하면
    - Then 생성한 지하철 노선 삭제가 성공한다.
  - [X] 기능 구현

# Step 2. 인수 테스트 리팩터링

### Step1 피드백

- [X] LineNotFoundException에서 매직 리터럴 관리
- [X] HTTP PUT METHOD 사용시 없는 경우 에러를 반환
- [X] CONVERTER 대신 DTO가 변환 책임을 갖도록 변경
- [X] /lines 공통 @RequestMapping 사용
- [ ] 테스트코드 검증부 모듈화
- [ ] 테스트에서 메소드 직접 사용 대신 @MehtodSource, @ArgumentSource 사용

### Step2 요구사항

- [ ] 지하철역과 지하철 노선 이름 중복 금지 기능 추가
  - [ ] Feature: 지하철역 관리 기능 인수 테스트 작성
    - Scenario: 중복이름으로 지하철역 생성
    - Given 지하철역 생성을 요청 하고
    - When 같은 이름으로 지하철역 생성을 요청 하면
    - Then 지하철역 생성이 실패한다.
    - [ ] 기능 구현
  - [ ] Feature: 지하철 노선 관리 기능 인수 테스트 작성
    - Scenario: 중복이름으로 지하철 노선 생성
    - Given 지하철 노선 생성을 요청 하고
    - When 같은 이름으로 지하철 노선 생성을 요청 하면
    - Then 지하철 노선 생성이 실패한다.
    - [ ] 기능 구현
- [ ] 새로운 기능 추가하면서 인수 테스트 리팩터링
