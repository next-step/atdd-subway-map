package google;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GoogleRestAssuredTest {

    @DisplayName("구글 페이지 접근 테스트 - Assertions.assertThat() 을 사용하여 검증")
    @Test
    void accessGoogle() {
        ExtractableResponse<Response> response = RestAssured
            .given()
            .when()
            .get("https://www.google.com")
            .then()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("구글 페이지 접근 테스트 - then().statusCode() 를 사용하여 검증")
    @Test
    void accessGoogleWithThenStatusCode() {
        RestAssured.baseURI = "https://www.google.com";

        RestAssured
            .given()
            .when()
            .get("https://www.google.com")
            .then()
            .statusCode(200);
    }
}