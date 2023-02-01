package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    public static final String GOOGLE_URL = "https://google.com";

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        // TODO: 구글 페이지 요청 구현
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when().get(GOOGLE_URL)
                .then().statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
