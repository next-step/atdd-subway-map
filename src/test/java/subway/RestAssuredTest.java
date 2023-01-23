package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RestAssuredTest {

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {

        final ExtractableResponse<Response> 구글_호출_응답 = 구글_호출_요청();

        assertAll(
                () -> assertThat(구글_호출_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(구글_호출_응답.body()).isNotNull()
        );
    }

    private ExtractableResponse<Response> 구글_호출_요청() {

        return RestAssured
                .given().baseUri("https://google.com").log().all()
                .when().get()
                .then().log().all()
                .extract();
    }
}