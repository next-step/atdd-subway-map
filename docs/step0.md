# 🚀 0단계 - 리뷰 사이클 연습
## 미션 설명
- 리뷰 사이클을 연습하기 위한 미션입니다.
- "https://google.com"로 요청을 보낼 경우 200응답 코드가 오는지를 검증하는 테스트를 작성하세요.
- 아래 리뷰 요청 방법 저장소: https://github.com/next-step/atdd-subway-map


## 리뷰 요청 방법
- 온라인 코드리뷰 요청 1단계 문서를 참고하여 실습 환경을 구축하세요.
- RestAssuredTest의 구글 페이지 접근 테스트를 작성하세요.
- 온라인 코드리뷰 요청 2단계 문서를 참고하여 리뷰 요청을 보내세요.


## 인수 테스트 메서드 시그니쳐

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        // TODO: 구글 페이지 요청 구현
        ExtractableResponse<Response> response = null;

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
```

## 힌트

### RestAssured 예시 코드

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