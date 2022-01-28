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

# 🚀 1단계 - 노선 관리 기능 구현
# 요구사항

---

- [x]  아래 인수 조건을 검증하는 인수 테스트 작성하기
- [x]  작성한 인수 테스트를 충족하는 기능 구현하기

## 인수 조건

```java
Feature: 지하철 노선 관리 기능

  Scenario: 지하철 노선 생성
    When 지하철 노선 생성을 요청 하면
    Then 지하철 노선 생성이 성공한다.
  
  Scenario: 지하철 노선 목록 조회
    Given 지하철 노선 생성을 요청 하고
    Given 새로운 지하철 노선 생성을 요청 하고
    When 지하철 노선 목록 조회를 요청 하면
    Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
    
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
```

## Request / Response

### 지하철 노선 생성

```java
POST /lines HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8

{
    "color": "bg-red-600",
    "name": "신분당선"
}
```

```java
HTTP/1.1 201 
Location: /lines/1
Content-Type: application/json
Date: Fri, 13 Nov 2020 00:11:51 GMT

{
    "id": 1,
    "name": "신분당선",
    "color": "bg-red-600",
    "createdDate": "2020-11-13T09:11:51.997",
    "modifiedDate": "2020-11-13T09:11:51.997"
}
```

### 지하철 노선 목록 조회

```java
GET /lines HTTP/1.1
accept: application/json
host: localhost:49468
```

```java
HTTP/1.1 200 
Content-Type: application/json
Date: Fri, 13 Nov 2020 00:11:51 GMT

[
    {
        "id": 1,
        "name": "신분당선",
        "color": "bg-red-600",
        "stations": [
            
        ],
        "createdDate": "2020-11-13T09:11:52.084",
        "modifiedDate": "2020-11-13T09:11:52.084"
    },
    {
        "id": 2,
        "name": "2호선",
        "color": "bg-green-600",
        "stations": [
            
        ],
        "createdDate": "2020-11-13T09:11:52.098",
        "modifiedDate": "2020-11-13T09:11:52.098"
    }
]
```

### 지하철 노선 조회

```java
GET /lines/1 HTTP/1.1
accept: application/json
host: localhost:49468
```

```java
HTTP/1.1 200 
Content-Type: application/json
Date: Fri, 13 Nov 2020 00:11:51 GMT

{
    "id": 1,
    "name": "신분당선",
    "color": "bg-red-600",
    "stations": [
        
    ],
    "createdDate": "2020-11-13T09:11:51.866",
    "modifiedDate": "2020-11-13T09:11:51.866"
}
```

### 지하철 노선 수정

```java
PUT /lines/1 HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8
content-length: 45
host: localhost:49468

{
    "color": "bg-blue-600",
    "name": "구분당선"
}
```

```java
HTTP/1.1 200 
Date: Fri, 13 Nov 2020 00:11:51 GMT
```

### 지하철 노선 삭제

```java
DELETE /lines/1 HTTP/1.1
accept: */*
host: localhost:49468
```

```java
HTTP/1.1 204 
Date: Fri, 13 Nov 2020 00:11:51 GMT
```

# 🚀 2단계 - 인수 테스트 리팩토링
# 요구사항

---

- [ ]  지하철역과 지하철 노선 이름 중복 금지 기능 추가
- [ ]  새로운 기능 추가하면서 인수 테스트 리팩토링

## 인수 조건

```java
Feature: 지하철역 관리 기능

...
  
  Scenario: 중복이름으로 지하철역 생성
    Given 지하철역 생성을 요청 하고
    When 같은 이름으로 지하철역 생성을 요청 하면
    Then 지하철역 생성이 실패한다.
```

```java
Feature: 지하철 노선 관리 기능

...
  
  Scenario: 중복이름으로 지하철 노선 생성
    Given 지하철 노선 생성을 요청 하고
    When 같은 이름으로 지하철 노선 생성을 요청 하면
    Then 지하철 노선 생성이 실패한다.
```

# 요구사항 설명

---

## 역, 노선 이름 중복 안되도록 처리하기

- 역 등록 시, 노선 등록 시 이미 등록된 이름으로 등록 요청 시 에러를 응답하도록 처리하기

## 인수 테스트 리팩토링

- 인수 테스트 작성 시 중복 코드가 많이 발생함.

### 힌트

- 메서드 분리와 CRUD 중복 제거 고려
- 메서드 분리 예시

```java
public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }
    ...
}
```

## 👄피드백

- [x]  **도메인이 자주 변하는 DTO에 의존하면 도메인 또한 자주 변경될 수 있기 때문에
  좋지 않다.**

  → 도메인은 자주 변하지 않고, DTO는 자주 변하는 객체이다. 이런 관계에서는
  많이 변하는 쪽이 많이 변하지 않는 곳에 의존해야 한다.
  UI가 서버 API에 의존하지 서버 API가 UI에 의존하지는 않는다.
  자동차 경주나 볼링 게임 같은 미션을 해보면서 확실히 느낀 부분이다.


```java
// 좋지 않음.
public void update(RequestDto request) {
    this.name = request.getName();
    this.color = request.getColor();
}

// 좋음.
public void update(String name, String color) {
    this.name = name;
    this.color = color;
}
```

- [x]  **http 응답 코드 개선하기**
- [x]  **예외 처리에서 try catch 대신 @ExceptionHandler 사용하기**
- [x]  예외 메시지를 View 객체인  ResponseStation과 같은 곳에 담지 말기. 연관성이 없는 것이다.
  → 따로 ExeptionDto 클래스를 반환 하도록 개선했음.
- [ ]  `**if (*ObjectUtils*.*isEmpty*(findLine)) {` 로 체크하지 말고 existsByXXX 쿼리 메서드로 개선하기**
- [ ]  이와 같은 것은 StationResponse 객체 안에서 정팩 팩토리 메서드로 분리?

```java
if (ObjectUtils.isEmpty(findStation)) {
            Station station = stationRepository.save(new Station(request.getName()));
            return createStationResponse(station);
```

- [x]  **매직 리터럴 없애기. 실수의 여지를 제공하게 됨.
  → 모듈화하거나, 멤버로 빼거나**
- [ ]  테스트 검증 부분도 모듈화가 고민
- [x]  노선 색깔도 변경했으니 색깔도 검증하기.

# 🚀 3단계 - 지하철 구간 관리
# 요구사항

---

- [x]  ~~지하철 노선 생성 시 필요한 인자 추가하기~~  
  → 구간 조회 구현하고 구간 조회해서 종점 번호와 거리 검증 테스트하기.  
  → 존재하지 않는 역은 예외 처리하기 : Service 로직
- [x]  지하철 노선에 구간을 등록하는 기능 구현
- [x]  지하철 노선에 구간을 제거하는 기능 구현
- [ ]  지하철 노선에 등록된 구간을 통해 역 목록을 조회하는 기능 구현
- [ ]  구간 등록 / 제거 시 예외 케이스에 대한 인수 테스트 작성

- 이번 단계는 요구사항을 가지고 직접 인수 조건을 도출하기
- 인수 조건 정의 → 인수 테스트 작성 → 기능 구현 사이클 지키며 미션 수행하기.
- 뼈대 코드는 패키지 구조를 비롯하여 마음대로 수정 가능.

## 인수 조건 도출

```java
Feature: 지하철 노선 관리

  Scenario 지하철 노선 생성
     Given 지하철역 생성을 요청하고,
     Given 새로운 지하철역 생성을 요청한다.
      When 지하철 노선 생성을 요청하면,
      Then 지하철 노선 생성이 성공한다.
      Then 생성된 노선에는 상행 종점과 하행 종점 번호와 번호 간 거리도 생성된다

Feature: 지하철 구간 관리
        
  Scenario 지하철 구간 생성
     Given 지하철역 생성을 요청하고,
       AND 새로운 지하철역 생성을 요청하고,
       AND 지하철 노선 생성을 요청한다.
       AND 새로운 지하철역 생성을 요청하고,
      When 기존의 지하철 노선에 구간 등록을 요청하면
      Then 상행역은 해당 노선에 등록되어있는 하행 종점역으로 등록된다
  
  Scenario 지하철 구간 삭제
     Given 지하철역 생성을 요청하고,
       AND 새로운 지하철역 생성을 요청하고,
       AND 지하철 노선 생성을 요청하고,
       AND 새로운 지하철역 생성을 요청하고,
       AND 구간 등록을 요청한다.
      When 구간 삭제를 요청한다.
      Then 하행 종점역이 제거된다.

```

# 요구사항 설명
## 지하철 노선 생성 시 필요한 인자 추가하기

- 노선 생성 시 두 종점역(상행 종점, 하행 종점) 추가하기
- 두 종점역 간의 거리 인자도 함께 받아서 생성하기
- 인수 테스트와 DTO등 수정이 필요함.

```java
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;       // 추가
    private Long downStationId;     // 추가
    private int distance;           // 추가
    ...
}
```

## 구간 등록 기능

- 지하철 노선에 구간을 동록하는 기능 구현
- 새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.
- 새로운 구간의 하행역은 현재 등록 되어있는 역일 수 없다.
- 새로운 구간 등록 시 위 조건에 부합하지 않는 경우 `예외처리`.

```java
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

## 구간 제거 기능

- 지하철 노선에 구간을 제거하는 기능 구현
- 지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.
- 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우)역을 삭제할 수 없다.
- 새로운 구간 제거 시 위 조건에 부합하지 않는 경우 `예외`처리

```java
DELETE /lines/1/sections?stationId=2 HTTP/1.1
accept: */*
host: localhost:52165
```

## 등록된 구간을 통해 역 목록 조회 기능

- 지하철 노선 조회 시 등록된 역 목록을 함께 응답
- 노선에 등록된 구간을 순서대로 정렬하여 상행 종점부터 하행 종점까지 목록을 응답하기

```java
HTTP/1.1 200 
Content-Type: application/json

[
    {
        "id": 1,
        "name": "신분당선",
        "color": "bg-red-600",
        "stations": [
            {
                "id": 1,
                "name": "강남역",
                "createdDate": "2020-11-13T12:17:03.075",
                "modifiedDate": "2020-11-13T12:17:03.075"
            },
            {
                "id": 2,
                "name": "역삼역",
                "createdDate": "2020-11-13T12:17:03.092",
                "modifiedDate": "2020-11-13T12:17:03.092"
            }
        ],
        "createdDate": "2020-11-13T09:11:51.997",
        "modifiedDate": "2020-11-13T09:11:51.997"
    }
]
```