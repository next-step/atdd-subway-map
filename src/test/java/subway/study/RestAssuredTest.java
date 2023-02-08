package subway.study;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {
    private static final int HTTPS_PORT = 443;
    public static final String GOOGLE_URL = "https://google.com";

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = HTTPS_PORT;
    }


    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        ExtractableResponse<Response> response =
                given().log().all()
                        .baseUri(GOOGLE_URL).
                when().
                        get().
                then().
                        log().status().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}