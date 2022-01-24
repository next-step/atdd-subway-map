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

# 1단계 - 노선 관리 기능 구현
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

## 알게된 것

---

### @PostMapping의 consumes, produces

- consumes
  - 요청 Content-Type을 제한할 수 있다.
  - 기본 값은 `application/json`이다. 어길 시 `415 Unsupported Media Type 에러`
- produces
  - 응답 Content-Type을 제한할 수 있다.
  - 즉, 요청 시 accept와 같다.
  
### Content-Type 필수 헤더 아님

- 실제로 신경 써본게 이번이 처음이라 가물가물 했는데 경험함.
## 다음 미션에서 고민해볼 것

---

- 요청 시 요청은 제대로 했는데 데이터가 없거나 그러면 어쩌지?  
  update, delete, 조회 전부
  그냥 예외 처리 없어도 되나??
- put하고 왜 응답에 변경된 값을 반환하지 않을까?  
  그리고 왜 200일까? 204가 맞지 않을까? → [이건 팀 규칙으로 생각해야 하나?](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/PUT#%EC%9D%91%EB%8B%B5)
  예외 처리도 필요 데이터 없을 시 201 응답

# 2단계 - 인수 테스트 리팩토링
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

# 알게된 것

---

## 나의 생각

### GET 요청은 성공 했으나, 데이터가 없을 때

이 경우는 구글링을 해봐도 케바케인 것 같다. [MDN](https://developer.mozilla.org/ko/docs/Web/HTTP/Status/204)을 살펴보면 PUT할 때 보통 사용을 한다고 한다.
→ 성공은 했는데 응답할 데이터가 필요 없을 때
ex) 엑셀 저장 버튼
200을 쓰고 빈 바디를 넘겨주는 경우도 많은 것 같은데 일단 명확하게 컨텐츠가 없다는 것이
더 맞다고 생각하기 때문에 `204 noContent를` 사용하기로 했다.

### JsonPath는 getter가 있어야 한다.

getter 기반으로 읽어오는 것이었다. 계속 예외 메시지를 넣었는데 null이 떠서 알게 되었다.

## 👄피드백

### 도메인이 자주 변하는 DTO에 의존하면 도메인 또한 자주 변경될 수 있기 때문에
좋지 않다.

도메인은 자주 변하지 않고, DTO는 자주 변하는 객체이다. 이런 관계에서는
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

# 질문

---

## DeleteMapping 시 데이터 없으면 뭘 줘야 할까??

## 위 알게된 것 1번의 경우 서비스에서 예외를 던지는 것이 좋은가?

서비스에서 예외를 던지고 컨트롤러에서 캐치해서 `존재하지 않는 라인 입니다`. 라는 예외이면
`200 상태 코드와 존재하지 않는 라인 입니다.` 라고 바디에 응답?

## PUT 요청 시 데이터 있으면 200 없으면 201처리

201은 신규 생성이니까. 맞지 않을까?

## update 시 변경된 내용을 json으로 응답 해줘야 하나?

어디서는 id만 반환하고, 어디서는 반환 안하고 등등.. 헷갈림. 팀바팀?

## 중복 생성 요청 e시 200으로 하고 예외 메시지 던진다.?

# 의문

---

## 노선 생성 시 302응답 했는데 왜 리다이렉션 하고 lines로 재요청 하지?