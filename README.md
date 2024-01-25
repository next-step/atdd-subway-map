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
- [ ] 지하철역 삭제 인수 테스트 작성
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
