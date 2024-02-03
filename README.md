# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션


# 🚀 3단계 - 지하철 구간 관리

## 기능

***

### 구간 등록 기능
* 지하철 노선에 구간을 등록하는 기능을 구현
* 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
* 이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.
* 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.

#### request
```http
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

### 구간 제거 기능
* 지하철 노선에 구간을 제거하는 기능 구현
* 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
* 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
* 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.

#### request
```http
DELETE /lines/1/sections?stationId=2 HTTP/1.1
accept: */*
host: localhost:52165
```

## TODO

***
### 인수테스트 작성
- [x] 구간 인수 테스트 클래스 생성
  ```
  SectionAcceptanceTest.java
  ``` 
* 구간 등록
  - [x] 구간 등록 성공 시나리오 작성
  ```
  Given 지하철 노선을 생성한다.
  When 생성한 지하철 노선의 하행 종점역부터 새로운 구간의 하행역을 등록하면
  Then 지하철 노선 조회 시, 새로운 하행 종점역을 확인할 수 있다.
  ```
  - [x] 구간 등록 실패 시나리오 작성
  ```
  Given 지하철 노선을 생성한다.
  When 생성한 지하철 노선의 하행 종점역부터 새로운 구간의 하행역을 등록하면
  Then 지하철 노선 조회 시, 새로운 하행 종점역을 확인할 수 있다.
  ```

  ```
  Given 지하철 노선을 생성한다. 생성한 지하철 노선에 구간을 등록한다.
  When 등록한 구간과 같은 구간을 등록하면
  Then 등록되지 않고 코드값 500 (Internal sever Error) 을 반환한다.
  ```
  - [x] 테스트 메소드 구현
- 구간 삭제
  - [x] 구간 삭제 성공 시나리오 작성
    ```
    Given 지하철 노선을 생성한다. 지하철 노선에 구간을 생성한다.
    When 해당 구간을 삭제하면
    Then 구간 목록 조회 시, 생성한 구간을 찾을 수 없다.
    ```
  - [x] 구간 삭제 실패 시나리오 작성
    ```
    Given 지하철 노선을 생성한다. 생성한 지하철 노선에 구간을 등록한다.
    When 등록한 구간과 같은 구간을 등록하면
    Then 등록되지 않고 코드값 500 (Internal sever Error) 을 반환한다.
    ```
    
    ```
    Given 지하철 노선을 생성한다. 생성한 지하철 노선에 구간을 등록한다.
    When 상행역이 해당 노선의 하행 종점역이 아닌 구간을 등록하면
    Then 등록되지 않고 코드값 500 (Internal sever Error) 을 반환한다.
    ```
  - [x] 테스트 메소드 구현
### 기능 구현
  - [x] Section 객체 구현
    - id, lineId, downStationId, upStationId, distance
  - [x] Line과 Section (1:N)
  - [x] lineService.createSection
  - [x] lineService.deleteSection
  - [x] 예외 처리
  - [x] 실패 테스트 에러 메시지 검증
  - [x] 테스트 리팩토링 : 이미 등록된 역과 노선 ID를 상수로 선언
