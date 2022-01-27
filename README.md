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

## 요구사항
- [x] 지하철 노선 생성 시 필요한 인자 추가하기
  - [x] 노선 생성 시 두 종점역(상행 종점, 하행 종점) 추가하기
  - [x] 두 종점역 간의 거리 인자도 함께 받아서 생성하게 하기
  - [x] 인수 테스트와 DTO 등 수정이 필요
  
- [x] 지하철 노선에 구간을 등록하는 기능 구현
  - [x] 새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.
  - [x] 새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.
  - [x] 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
  
- [x] 지하철 노선에 구간을 제거하는 기능 구현
  - [x] 지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.
  - [x] 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
  - [x] 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
  
- [x] 지하철 노선에 등록된 구간을 통해 역 목록을 조회하는 기능 구현
  - [x] 노선에 등록된 구간을 순서대로 정렬하여 상행 종점부터 하행 종점까지 목록을 응답하기
   
- [x] 구간 등록 / 제거 시 예외 케이스에 대한 인수 테스트 작성

## 인수 조건
Feature: 지하철 노선 관리 기능

    Scenario: 지하철 노선 생성
        When 지하철 노선 생성을 요청하면
        Then 지하철 노선 생성이 성공한다.

    Scenario: 지하철 노선 목록 조회
        Given 지하철 노선 생성을 요청하고
        Given 새로운 지하철 노선 생성을 요청하고
        When 지하철 노선 목록 조회를 요청 하면
        Then 두 노선이 포함된 지하철 노선 목록을 응답받는다.

    Scenario: 지하철 노선 조회
        Given 지하철 노선 생성을 요청 하고
        When 생성한 지하철 노선 조회를 요청 하면
        Then 생성한 지하철 노선을 응답받는다

    Scenario: 지하철 노선 수정
        Given 지하철 노선 생성을 요청 하고
        When 지하철 노선의 정보 수정을 요청 하면
        Then 지하철 노선의 정보 수정은 성공한다.

    Scenario: 지하철 노선 삭제
        Given 지하철 노선 생성을 요청 하고
        When 생성한 지하철 노선 삭제를 요청 하면
        Then 생성한 지하철 노선 삭제가 성공한다.

    Scenario: 중복이름으로 지하철 노선 생성
        Given 지하철 노선 생성을 요청 하고
        When 같은 이름으로 지하철 노선 생성을 요청 하면
        Then 지하철 노선 생성이 실패한다.

    Scenario: 노선 구간 등록
        Given 지하철 노선 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        When 생성된 지하철 노선에 생성된 지하철 역으로 구간 등록을 요청하면
        Then 지하철 노선에 구간 등록이 성공한다 

    Scenario: 이미 구간 등록된 상행역을 상행역 구간 등록
        Given 지하철 노선 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        When 이미 등록된 역으로 상행역으로 구간 등록을 요청하면
        Then 지하철 노선에 구간 등록이 실패한다. 

    Scenario: 이미 구간 등록된 역을 하행역 재등록
        Given 지하철 노선 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        When 이미 등록된 역으로 하행역으로 구간 등록을 요청하면
        Then 지하철 노선에 구간 등록이 실패한다. 
 
    Scenario: 노선 구간 삭제
        Given 지하철 노선 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 생성된 지하철 노선에 구간 등록을 요청하고
        Given 생성된 지하철 노선에 구간 등록을 요청하고
        When 해당 노선의 마지막 역으로 구간 삭제를 요청하면
        Then 지하철 노선에 구간이 삭제된다.

    Scenario: 노선 구간 중간 삭제
        Given 지하철 노선 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 생성된 지하철 노선에 구간 등록을 요청하고
        Given 생성된 지하철 노선에 구간 등록을 요청하고
        When 해당 노선의 중간역을 구간 삭제를 요청하면
        Then 지하철 노선 삭제 요청이 실패한다.
 
    Scenario: 상행 종점역과 하행 종점역만 있는 노선 구간(1개) 삭제
        Given 지하철 노선 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 지하철 역 생성을 요청하고
        Given 생성된 지하철 노선에 구간 등록을 요청하고
        When 해당 노선의 마지막 역으로 구간 삭제를 요청하면
        Then 지하철 노선 삭제 요청이 실패한다.

Feature: 지하철역 관리 기능
    
    Scenario: 지하철역 생성
        When 지하철역 생성을 요청 하면
        Then 지하철역 생성이 성공한다.
        
    Scenario: 지하철역 목록 조회
        Given 지하철역 생성을 요청 하고
        Given 새로운 지하철역 생성을 요청 하고
        When 지하철역 목록 조회를 요청 하면
        Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
    
    Scenario: 지하철역 삭제
        Given 지하철역 생성을 요청 하고
        When 생선한 지하철역 삭제를 요청 하면
        Then 생선한 지하철역 삭제가 성공한다.

    Scenario: 중복이름으로 지하철역 생성
        Given 지하철역 생성을 요청 하고
        When 같은 이름으로 지하철역 생성을 요청 하면
        Then 지하철역 생성이 실패한다.
