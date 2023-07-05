package acceptance;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

import config.AcceptanceTestConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class RestAssuredTest extends AcceptanceTestConfig {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        ExtractableResponse<Response> response = when()
            .get("https://google.com")
            .then()
            .statusCode(200).extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}