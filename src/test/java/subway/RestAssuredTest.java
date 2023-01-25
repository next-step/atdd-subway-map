package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {
    private String url;

    @BeforeEach
    void setUp(){
        url = "https://google.com";
    }

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        // TODO: 구글 페이지 요청 구현
//        ExtractableResponse<Response> response = get(url).then().extract();
        ExtractableResponse<Response> response = when().get(url).then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}