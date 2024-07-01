package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
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
        // testing: 구글 페이지 요청
        ExtractableResponse<Response> response =
                given().
                    param("q", "구글페이지+요청").
                when().
                    get("https://www.google.com/search").
                then().
                    extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo("text/html; charset=ISO-8859-1");
    }
}