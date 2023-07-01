package line;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.SubwayApplication;
import subway.line.LineCreateRequest;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private static final String API_CREATE_LINE = "/lines";

    @DisplayName("노선을 생성한다")
    @Test
    void createLine() {
        // given
        LineCreateRequest request = new LineCreateRequest("신분당선", "bg-red-600", 1, 2, 10);

        // when
        ExtractableResponse<Response> createdResponse = RestAssured.given().log().all()
                                                                   .body(request)
                                                                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                   .when().post(API_CREATE_LINE)
                                                                   .then().log().all()
                                                                   .extract();

        // then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createdResponse.jsonPath().getString("name")).isEqualTo("신분당선");
    }
}
