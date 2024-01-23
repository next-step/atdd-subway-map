package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    @DisplayName("응답 객체로 구글 페이지 접근 테스트")
    @Test
    void accessGoogle_response() {
        // TODO: 구글 페이지 요청 구현
        // Response 반환 (extract method 사용)
        ExtractableResponse<Response> response = when()
                .get("https://google.com")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("statusCode method로 구글 페이지 접근 테스트")
    @Test
    void accessGoogle_statusCode() {
        // TODO: 구글 페이지 요청 구현
        // statusCode method로 검증
        when()
                .get("https://google.com")
                .then()
                .statusCode(200);
    }
}
