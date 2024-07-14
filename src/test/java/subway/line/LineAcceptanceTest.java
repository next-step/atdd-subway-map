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
        Long 신사역 = 지하철역_생성("신사역");
        Long 논현역 = 지하철역_생성("논현역");

        ExtractableResponse<Response> response = 지하철노선_생성(
            createParams("신분당선", "bg-red-600", 신사역, 논현역, 10L));

        //then
        String name = response.jsonPath().getString("name");
        assertThat(name).isEqualTo("신분당선");

        List<Long> stationIds = response.jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds).containsExactlyInAnyOrder(신사역, 논현역);
    }

    /**
     * Given 여러개의 지하철 노선이 등록되어 있고
     * When 지하철 노선 목록을 조회하면
     * Then 모든 지하철 노선 목록이 조회된다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        //given
        Long 신사역 = 지하철역_생성("신사역");
        Long 논현역 = 지하철역_생성("논현역");
        Long 신논현역 = 지하철역_생성("신논현역");
        Long 강남역 = 지하철역_생성("강남역");
        Long 역삼역 = 지하철역_생성("역삼역");

        지하철노선_생성(createParams("신분당선", "bg-red-600", 신사역, 논현역, 10L));
        지하철노선_생성(createParams("신분당선", "bg-red-600", 논현역, 신논현역, 10L));
        지하철노선_생성(createParams("신분당선", "bg-red-600", 신논현역, 강남역, 10L));
        지하철노선_생성(createParams("2호선", "bg-green-600", 강남역, 역삼역, 10L));

        //when
        ExtractableResponse<Response> response = 지하철노선_목록조회();

        //then
        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames).hasSize(2);
        assertThat(lineNames).containsExactlyInAnyOrder("신분당선", "2호선");

    }

    /**
     * Given 특정 지하철 노선이 등록되어 있고
     * When 해당 노선을 조회하면
     * Then 해당 지하철 노선 목록이 조회된다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        //given
        Long 신사역 = 지하철역_생성("신사역");
        Long 논현역 = 지하철역_생성("논현역");
        Long 강남역 = 지하철역_생성("강남역");

        String id = 지하철노선_생성(createParams("신분당선", "bg-red-600", 신사역, 논현역, 10L))
            .jsonPath().getString("id");
        지하철노선_생성(createParams("신분당선", "bg-red-600", 논현역, 강남역, 10L));

        //when
        ExtractableResponse<Response> response = 지하철노선_조회(id);

        //then
        String lineName = response.jsonPath().getString("name");
        assertThat(lineName).isEqualTo("신분당선");

        List<String> stationNames = response.jsonPath().getList("stations.name", String.class);
        assertThat(stationNames).containsExactlyInAnyOrder("신사역", "논현역", "강남역");
    }

    /**
     * Given 특정 지하철 노선이 등록되어 있고
     * When 해당 노선을 수정하면
     * Then 해당 노선의 정보가 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        //given
        Long 신사역 = 지하철역_생성("신사역");
        Long 논현역 = 지하철역_생성("논현역");

        String id = 지하철노선_생성(createParams("신분당선", "bg-red-600", 신사역, 논현역, 10L))
            .jsonPath().getString("id");

        //when
        ExtractableResponse<Response> response = 지하철노선_수정(id, updateParams("1호선", "bg-blue-600"));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        String lineName = 지하철노선_조회(id).jsonPath().getString("name");
        assertThat(lineName).isEqualTo("1호선");

    }

    /**
     * Given 특정 지하철 노선이 등록되어 있고
     * When 해당 노선을 삭제하면
     * Then 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        //given
        Long 신사역 = 지하철역_생성("신사역");
        Long 논현역 = 지하철역_생성("논현역");
        String id = 지하철노선_생성(createParams("신분당선", "bg-red-600", 신사역, 논현역, 10L))
            .jsonPath().getString("id");

        //when
        ExtractableResponse<Response> response = 지하철노선_삭제(id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<String> lineNames = 지하철노선_목록조회().jsonPath().getList("name", String.class);
        assertThat(lineNames).doesNotContain("신분당선");

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

    private static Map<String, Object> updateParams(String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    private static ExtractableResponse<Response> 지하철노선_생성(Map<String, Object> params) {

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

    private static Long 지하철역_생성(String stationName) {
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

    private static ExtractableResponse<Response> 지하철노선_목록조회() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
        return response;
    }

    private static ExtractableResponse<Response> 지하철노선_조회(String id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines/" + id)
            .then().log().all()
            .extract();
        return response;
    }

    private static ExtractableResponse<Response> 지하철노선_수정(String id,
        Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/" + id)
            .then().log().all()
            .extract();
        return response;
    }

    private static ExtractableResponse<Response> 지하철노선_삭제(String id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().delete("/lines/" + id)
            .then().log().all()
            .extract();
        return response;
    }
}
