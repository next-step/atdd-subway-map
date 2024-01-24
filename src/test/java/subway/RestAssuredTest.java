package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    @DisplayName("구글 페이지에 요청을 보내면 status 200을 응답 받는다")
    @Test
    void accessGoogle() {
        // TODO: 구글 페이지 요청 구현
        ExtractableResponse<Response> response = when()
                .get("https://www.google.com")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}