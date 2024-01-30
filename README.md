# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 인수 테스트와 E2E 테스트
## 0단계 - 리뷰 사이클 연습
### 요구사항
- [x]  "https://google.com"로 요청을 보낼 경우 200응답 코드가 오는지를 검증하는 테스트를 작성
#### RestAssured 예시 코드
- GET /lotto?x=y 요청을 보낼 경우
- 응답 코드를 400을 예상하고
- 응답 바디 중 "lotto.lottoId"의 값이 6임을 예상한다.
```java
given().
        param("x", "y").
when().
        get("/lotto").
then().
        statusCode(400).
        body("lotto.lottoId", equalTo(6));
```


## 1단계 - 지하철역 인수 테스트 작성
### 요구사항
- [x] 지하철역 목록 인수 테스트 작성
  - API Request
    ```shell
    GET /stations HTTP/1.1
    Accept: application/json
    Host: localhost:8080
    ```
  - API Response
    ```shell
    HTTP/1.1 200 OK
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Header
    Content-Type: application/json
    Content-Length: 167
    
    [ {
      "id" : 1,
      "name" : "지하철역이름"
    }, {
      "id" : 2,
      "name" : "새로운지하철역이름"
    }, {
      "id" : 3,
      "name" : "또다른지하철역이름"
    } ]
    ```
  - [x] dummy data 를 넣는다.
  - [x] "/stations" 로 Get 요청을 보낸다.
  - [x] statusCode 가 200 OK 인지 확인한다.
  - [x] jsonPath 를 이용해 넣었던 dummy data 가 있는지 확인한다.
- [x] 지하철역 삭제 인수 테스트 작성
  - API Request
      ```shell
      DELETE /stations/1 HTTP/1.1
      Host: localhost:8080
      ```
  - API Response
    ```shell
    HTTP/1.1 204 No Content
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Header
    ```
  - [x] dummy data 를 넣는다.
  - [x] "/stations/1" 로 Delete 요청을 보낸다.
  - [x] statusCode 가 204 No Content 인지 확인한다.
  - [x] "/stations" 로 Get 요청을 보낸다.
  - [x] jsonPath 를 이용해 해당 지하철역이 삭제 되었는지 확인한다.


## 2단계 - 지하철 노선 관리
### 요구사항
- [x] 지하철 노선 생성 - "POST /lines"
  > When 지하철 노선을 생성하면 <br>
  Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
  - Request Data
    ```json
    {
      "name" : "신분당선",
      "color" : "bg-red-600",
      "upStationId" : 1,
      "downStationId" : 2,
      "distance" : 10
    }
    ```
  - Response Data
    ```json
    {
      "id" : 1,
      "name" : "신분당선",
      "color" : "bg-red-600",
      "stations" : [
        {
          "id": 1,
          "name": "지하철역"
        },
        {
          "id": 2,
          "name": "새로운지하철역"
        } 
      ]
    }
    ```
  - [x] "/lines" 로 Post 요청을 보낸다.
  - [x] 요청 data validation
    - [x] name 이 없다면 Exception 을 던진다.
    - [x] color 가 없다면 Exception 을 던진다.
    - [x] 상행역, 하행역 없다면 Exception 을 던진다.
    - [x] distance 가 0 이하이면 Exception 을 던진다.
    - [x] 상행역, 하행역이 같다면 Exception 을 던진다.
  - [x] Line 을 저장한다.
    - [x] 저장시 상행역, 하행역이 저장되어있지 않으면 Exception 을 던진다.
  - [x] 저장 후 응답 객체를 만든다.
    - [x] 응답 객체에 상행역, 하행역 정보를 이용해 응답 객체에 포힘시킨다.


- [x] 지하철 노선 목록 조회 - "GET /lines"
  > Given 2개의 지하철 노선을 생성하고<br>
  When 지하철 노선 목록을 조회하면<br>
  Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
  - Response Data
    ```json
    [
      {
        "id" : 1,
        "name" : "신분당선",
        "color" : "bg-red-600",
        "stations" : [
          {
            "id": 1,
            "name": "지하철역"
          },
          {
            "id": 2,
            "name": "새로운지하철역"
          } 
        ]
      },
      {
        "id" : 2,
        "name" : "분당선",
        "color" : "bg-green-600",
        "stations" : [
          {
            "id": 1,
            "name": "지하철역"
          },
          {
            "id": 3,
            "name": "또다른지하철역"
          } 
        ]
      }
    ]
    ```
  - [x] "/lines" 로 Get 요청을 보낸다.
  - [x] Line 들을 상행역, 하행역과 함께 조회한다.
  - [x] 응답 객체로 변환해 반환한다.


- [x] 지하철 노선 조회 - "GET /lines/{id}"
  > Given 지하철 노선을 생성하고<br>
  When 생성한 지하철 노선을 조회하면<br>
  Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
  - Response Data
    ```json
    {
      "id" : 1,
      "name" : "신분당선",
      "color" : "bg-red-600",
      "stations" : [
        {
          "id": 1,
          "name": "지하철역"
        },
        {
          "id": 2,
          "name": "새로운지하철역"
        } 
      ]
    }
    ```
    - [x] "/lines/{id}" 로 Get 요청을 보낸다.
    - [x] 해당 id를 가진 Line 정보를 상행역, 하행역과 함께 조회한다.
      - [x] 해당 id를 가진 Line 이 없으면 Exception 을 던진다.
    - [x] 응답 객체로 변환해 반환한다.


- [x] 지하철 노선 수정 - "PUT /lines/{id}"
  > Given 지하철 노선을 생성하고<br>
  When 생성한 지하철 노선을 수정하면<br>
  Then 해당 지하철 노선 정보는 수정된다
  - Request Data
    ```json
    {
      "name" : "신분당선",
      "color" : "bg-red-600"
    }
    ```
    - [x] "/lines/{id}" 로 put 요청을 보낸다.
    - [x] 요청 data validation
      - [x] name 이 없다면 Exception 을 던진다.
      - [x] color 가 없다면 Exception 을 던진다.
    - [x] 해당 id를 가진 Line 정보를 조회한다.
      - [x] 해당 id를 가진 Line 이 없으면 Exception 을 던진다.
    - [x] 요청 객체를 바탕으로 객체를 수정한다.


- [x] 지하철 노선 삭제 - "DELETE /lines/{id}"
  > Given 지하철 노선을 생성하고<br>
  When 생성한 지하철 노선을 삭제하면<br>
  Then 해당 지하철 노선 정보는 삭제된다
  - [x] "/lines/{id}" 로 delete 요청을 보낸다.
  - [x] 해당 id 를 가진 노선정보를 삭제한다.


- [x] 인수 테스트 격리
  - EntityManager 이용

## 2단계 - 지하철 구간 관리
### 요구사항
- [x] 구간 등록 기능 - "POST /lines/{lineId}/sections"
  ```json
  // Request body
  {
      "downStationId": "4",
      "upStationId": "2",
      "distance": 10
  }
  ```
  - 성공 시나리오
  > When 지하철 구간을 생성하면<br>
  Then 지하철 노선 조회시 구간 정보와 함께 조회할 수 있다.
  - 실패 시나리오
  > When 지하철 구간을 생성하는데<br>
  > When 구간 상행역이 해당 노선의 하행 종점역이 아니라면<br>
  Then 에러가 난다.
  
  > When 지하철 구간을 생성하는데<br>
  > When 구간 하행역이 해당 노선에 등록되어 있다면<br>
  Then 에러가 난다.

- [ ] 구간 제거 기능 - "DELETE /lines/{lineId}/stations?stationId=:id"
  - 성공 시나리오
  > Given 지하철 구간을 생성하고<br> 
  When 지하철 구간을 제거하면<br>
  Then 지하철 노선 조회시 해당 구간 정보가 제외되고 조회된다.
  - 실패 시나리오
  > When 지하철 구간을 제거하는데<br>
  When 해당 지하철 구간이 한개만 남아 있다면<br>
  Then 에러가 난다.

  > Given 지하철 구간을 생성하고<br>
  When 지하철 구간을 제거하는데<br>
  When 해당 지하철 구간이 마지막 구간이 아니면<br>
  Then 에러가 난다.
