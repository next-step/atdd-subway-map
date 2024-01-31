# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 0단계 - 리뷰 사이클 연습

### 기능 요구사항

- [x] `https://google.com` 로 요청을 보낼 경우 200응답 코드가 오는지를 검증하는 테스트를 작성한다.

<br>

## 🚀 1단계 - 지하철역 인수 테스트 작성

### 기능 요구사항

- [x] 지하철역 인수 테스트를 완성하세요. 
  - [x] 지하철역 목록 조회 인수 테스트 작성하기 
  - [x] 지하철역 삭제 인수 테스트 작성하기

<br>

## 🚀 2단계 - 지하철 노선 관리

### 기능 요구사항

- 요구사항 설명에서 제공되는 인수 조건을 기반으로 지하철 노선 관리 기능을 구현하세요. 
- 인수 조건을 검증하는 인수 테스트를 작성하세요. 
- 기능 목록 
- [x] 지하철 노선 생성 
- [x] 지하철 노선 목록 조회 
- [x] 지하철 노선 조회 
- [x] 지하철 노선 수정 
- [x] 지하철 노선 삭제

<br>

## 🚀 3단계 - 지하철 구간 관리

### 요구사항

- [ ] 구간 등록 기능
  - [ ] 지하철 노선에 구간을 등록하는 기능을 구현
    - [ ] 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
    - [ ] 이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.
    - [ ] 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.

```
POST /lines/1/sections HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8
host: localhost:52165

{
    "downStationId": "4",
    "upStationId": "2",
    "distance": 10
}
```

- [ ] 구간 제거 기능
  - [ ] 지하철 노선에 구간을 제거하는 기능 구현
    - [ ] 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
    - [ ] 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
    - [ ] 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.

```
DELETE /lines/1/sections?stationId=2 HTTP/1.1
accept: */*
host: localhost:52165
```
