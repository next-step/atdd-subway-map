package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("노선 관리 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    /**
     * When 새로운 지하철 노선을 입력하고, 관리자가 노선을 생성하면
     * Then 해당 노선이 생성된다.
     * Then 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Long upStationId = addStation("신사역");
        Long downStationId = addStation("논현역");
        Map<String, Object> params = createParams("신분당선", "bg-red-600", upStationId, downStationId, 10L);

        ExtractableResponse<Response> response = addLine(params);

        //then
        String name = response.jsonPath().getString("name");
        assertThat(name).isEqualTo("신분당선");

        List<Long> stationIds = response.jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds).containsExactlyInAnyOrder(upStationId, downStationId);
    }

    /**
     * Given 여러개의 지하철 노선이 등록되어 있고
     * When 지하철 노선 목록을 조회하면
     * Then 모든 지하철 노선 목록이 조회된다.
     */
    @DisplayName("지하철 노선을 목록을 조회한다.")
    @Test
    void showLines() {
        //given
        addLine(createParams("신분당선", "bg-red-600", addStation("신사역"), addStation("논현역"), 10L));
        addLine(createParams("신분당선", "bg-red-600", addStation("논현역"), addStation("신논현역"), 10L));
        addLine(createParams("신분당선", "bg-red-600", addStation("신논현역"), addStation("강남역"), 10L));
        addLine(createParams("2호선", "bg-green-600", addStation("강남역"), addStation("역삼역"), 10L));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        //then
        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames).hasSize(2);
        assertThat(lineNames).containsExactlyInAnyOrder("신분당선", "2호선");

    }

    private static Map<String, Object> createParams(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    private static ExtractableResponse<Response> addLine(Map<String, Object> params) {

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private static Long addStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        String location = response.header("Location");
        Long stationId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        return stationId;
    }

}
