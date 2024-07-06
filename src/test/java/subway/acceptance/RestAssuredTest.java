package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.internal.BaseTestSetup;

import static org.assertj.core.api.Assertions.assertThat;

public class RestAssuredTest extends BaseTestSetup {

    @Disabled()
    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().get("https://www.google.com")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}