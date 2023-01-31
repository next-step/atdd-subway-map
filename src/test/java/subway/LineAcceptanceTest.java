package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다 => 지하철 노선 목록의 이름 중에 1호선이 있다.
     */
    @Test
    void createLine() {
        //when
        String lineName = "1호선";
        String lineColor = "파란색";

        final Map<String, String> params = new HashMap<>();
        putParams(params, lineName, lineColor);

        getSaveLineResponse(params);

        //then
        ExtractableResponse<Response> readResponses = getReadResponses();

        Assertions.assertThat(readResponses.body().jsonPath().getList("name")).contains(lineName);
    }

    private ExtractableResponse<Response> getReadResponses() {
        ExtractableResponse<Response> readResponse = RestAssured
                .given()
                .when().get("/lines")
                .then().log().all()
                .extract();

        Assertions.assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return readResponse;

    }

    private void putParams(Map<String, String> params, String lineName, String lineColor) {
        params.put("name", lineName);
        params.put("color", lineColor);
    }

    private void getSaveLineResponse(Map<String, String> params) {
        ExtractableResponse<Response> saveResponse = RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        Assertions.assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
