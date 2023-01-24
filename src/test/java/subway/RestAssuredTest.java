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
class RestAssuredTest {

    private static final String GOOGLE_URL = "https://google.com";

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        // given & when
        ExtractableResponse<Response> response = RestAssured
                .when().get(GOOGLE_URL)
                .then().statusCode(HttpStatus.OK.value())
                .extract();

        // then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
    }
}