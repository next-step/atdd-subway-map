package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;

import static nextstep.subway.acceptance.StationRequestCollection.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    Long upStationId;
    Long downStationId;

    @BeforeEach
    void init() {
        upStationId = 지하철역_생성("강남역").jsonPath().getLong("id");
        downStationId = 지하철역_생성("건대입구역").jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선을 생성")
    public void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성("2호선", "bg-green-600", upStationId, downStationId, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        JsonPath responseBody = response.jsonPath();
        assertThat(responseBody.getLong("id")).isNotNull();
        assertThat(responseBody.getString("name")).isEqualTo("2호선");
        assertThat(responseBody.getString("color")).isEqualTo("bg-green-600");
        assertThat(responseBody.getList("stations.name")).contains("강남역", "건대입구역");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("전체 지하철 노선 목록 조회")
    public void searchLines() {
        // given
        ExtractableResponse<Response> response1 = 지하철_노선_생성("2호선", "bg-green-600", upStationId, downStationId, 10);
        ExtractableResponse<Response> response2 = 지하철_노선_생성("1호선", "bg-blue-600", upStationId, downStationId, 7);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath responseBody = response.jsonPath();
        assertThat(responseBody.getList("name")).contains("1호선", "2호선");
        assertThat(responseBody.getList("name")).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("단일 지하철 노선 조회")
    public void searchLine() {
        // given
        long lineId = 지하철_노선_생성("2호선", "bg-green-600", upStationId, downStationId, 10).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_단일_노선_조회(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath responseBody = response.jsonPath();
        assertThat(responseBody.getLong("id")).isNotNull();
        assertThat(responseBody.getString("name")).isEqualTo("2호선");
        assertThat(responseBody.getString("color")).isEqualTo("bg-green-600");
        assertThat(responseBody.getList("stations.name")).contains("강남역", "건대입구역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    public void editLine() {
        // given
        long lineId = 지하철_노선_생성("2호선", "bg-green-600", upStationId, downStationId, 10).jsonPath().getLong("id");

        // when
        int editStatusCode = 지하철_노선_수정(lineId, "다른 2호선", "연두색");

        // then
        assertThat(editStatusCode).isEqualTo(HttpStatus.OK.value());
    }

    private int 지하철_노선_수정(long lineId, String name, String color) {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("name", name);
        requestParam.put("color", color);


        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract()
                .statusCode();
    }

    private ExtractableResponse<Response> 지하철_단일_노선_조회(long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_생성(String lineName, String color, Long upStationId, Long downStationId, Integer distance) {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("name", lineName);
        requestParam.put("color", color);
        requestParam.put("upStationId", upStationId.toString());
        requestParam.put("downStationId", downStationId.toString());
        requestParam.put("distance", distance.toString());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
