package subway;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.nio.charset.StandardCharsets;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {
    @BeforeAll
    static void beforeAll() {
        RestAssured.config =
            new RestAssuredConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
    }


    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        ExtractableResponse<Response> response =
            given()
                .baseUri("https://google.com")
            .when()
                .contentType(ContentType.HTML.withCharset(StandardCharsets.UTF_8))
                .accept(ContentType.HTML.withCharset(StandardCharsets.UTF_8))
                .get( "/")
            .then()
                .statusCode(HttpStatus.OK.value())
                .log()
                .all()
                .extract();

        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.OK.value());
    }
    @DisplayName("구글 검색 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"atdd"})
    void searchByGoogle(String searchQuery) {
        ExtractableResponse<Response> response =
            given()
                .baseUri("https://google.com")
                .param("q", searchQuery)
            .when()
                .contentType(ContentType.HTML.withCharset(StandardCharsets.UTF_8))
                .accept(ContentType.HTML.withCharset(StandardCharsets.UTF_8))
                .get( "/search")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.containsStringIgnoringCase(searchQuery))
                .log()
                .all()
                .extract();

        assertThat(response.statusCode())
            .isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString())
            .containsIgnoringCase(searchQuery);
    }
}
