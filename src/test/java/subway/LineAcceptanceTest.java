package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql("/test-sql/station-insert.sql")
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        callApiCreateLines(params);

        // then
        List<String> actual = callApiFindLines().jsonPath().getList("name", String.class);
        String expected = "신분당선";
        assertThat(actual).containsAnyOf(expected);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철역들의 목록을 조회한다.")
    @Test
    void findLines() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");
        callApiCreateLines(params);

        params = new HashMap<>();
        params.put("name", "0호선");
        params.put("color", "bg-red-100");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");
        callApiCreateLines(params);

        // when
        List<LineResponse> linesResponse = callApiFindLines().jsonPath().getList(".", LineResponse.class);

        // then
        int actual = linesResponse.size();
        int expected = 2;
        assertThat(actual).isEqualTo(expected);
    }

    private ExtractableResponse<Response> callApiCreateLines(Map<String, String> params) {
        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> callApiFindLines() {
        return given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }
}
