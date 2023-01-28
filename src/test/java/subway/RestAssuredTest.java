package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class RestAssuredTest extends AcceptanceTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    @Disabled
    void accessGoogle() {
        ExtractableResponse<Response> response = RestAssured
            .when().get("https://google.com")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
