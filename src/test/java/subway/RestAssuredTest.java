package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RestAssuredTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {

        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .get("https://www.google.com")
                .then().statusCode(200)
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("구글 페이지 접근 실패 테스트")
    @Test
    void accessFailGoogle() {
        try {
            ExtractableResponse<Response> response = RestAssured
                    .given()
                    .when().get("https://www.failaccesstogoogle.com")
                    .then().statusCode(500)
                    .extract();
        } catch (Exception e) {
            if (e instanceof UnknownHostException) {
                return;
            }
        }
    }

}