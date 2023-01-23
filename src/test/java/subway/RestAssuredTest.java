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

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        // given
        final String 요청_URI = "https://google.com";

        // when
        final ExtractableResponse<Response> 구글_페이지_응답 = RestAssured
                .given().log().all()
                .when().get(요청_URI)
                .then().log().all()
                .extract();

        // then
        assertThat(구글_페이지_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}