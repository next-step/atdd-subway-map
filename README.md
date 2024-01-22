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
