package subway;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.rest.Rest;

class RestAssuredTest {
    @BeforeAll
    static void beforeAll() {
        RestAssured.reset();
    }

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        ExtractableResponse<Response> extractableResponse = Rest.builder().get("https://www.google.co.kr");
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
