package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
@Sql({"classpath:subway.init.sql"})
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
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createLine() {
        // When
        String 첫번째역 = "지하철역";
        String 두번째역 = "새로운지하철역";

        long firstStationId = createStationRequest(첫번째역);
        long secondStationId = createStationRequest(두번째역);

        ExtractableResponse<Response> 첫번째노선_생성_응답 = createLineRequest("신분당선", "bg-red-600", firstStationId, secondStationId, 10);

        // Then ?? When ??
        List<String> stationNames = 첫번째노선_생성_응답.jsonPath().getList("stations.name", String.class);
        assertAll(
                () -> assertThat(첫번째노선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(stationNames).containsAnyOf("지하철역", "새로운지하철역")
        );

        // Then
        ExtractableResponse<Response> 지하철_노선_목록_조회 = findAllLinesRequest();
        List<String> 신분당선_역이름 = 지하철_노선_목록_조회.jsonPath().get("stations[0].name");

        assertAll(
                () -> assertThat(신분당선_역이름).hasSize(2),
                () -> assertThat(신분당선_역이름).containsAnyOf("지하철역", "새로운지하철역")
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines() {
        // Given
        String 첫번째역 = "지하철역";
        String 두번째역 = "새로운지하철역";
        String 세번째역 = "또다른지하철역";

        long firstStationId = createStationRequest(첫번째역);
        long secondStationId = createStationRequest(두번째역);
        long thirdStationId = createStationRequest(세번째역);

        createLineRequest("신분당선", "bg-red-600", firstStationId, secondStationId, 10);
        createLineRequest("분당선", "bg-green-600", firstStationId, thirdStationId, 20);

        // When
        ExtractableResponse<Response> 노선_전체조회_응답 = findAllLinesRequest();
        List<String> 신분당선_역이름 = 노선_전체조회_응답.jsonPath().get("stations[0].name");
        List<String> 분당선_역이름 = 노선_전체조회_응답.jsonPath().get("stations[1].name");

        // Then
        assertAll(
                () -> assertThat(신분당선_역이름).hasSize(2),
                () -> assertThat(신분당선_역이름).containsAnyOf("지하철역", "새로운지하철역"),
                () -> assertThat(분당선_역이름).hasSize(2),
                () -> assertThat(분당선_역이름).containsAnyOf("지하철역", "또다른지하철역")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void getLineById() {
        // Given
        String 첫번째역 = "첫번째역";
        String 두번째역 = "새로운지하철역";

        long firstStationId = createStationRequest(첫번째역);
        long secondStationId = createStationRequest(두번째역);

        ExtractableResponse<Response> 신분당선 = createLineRequest("신분당선", "bg-red-600", firstStationId, secondStationId, 10);

        // When
        long lineId = 신분당선.jsonPath().getLong("id");
        ExtractableResponse<Response> 신분당선_조회_응답 = findLineByIdAPIResponse(lineId);

        List<String> stationNames = 신분당선_조회_응답.jsonPath().getList("stations.name", String.class);

        // Then
        assertThat(stationNames).containsAnyOf("첫번째역", "새로운지하철역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        // Given
        String 첫번째역 = "첫번째역";
        String 두번째역 = "새로운지하철역";

        long firstStationId = createStationRequest(첫번째역);
        long secondStationId = createStationRequest(두번째역);

        ExtractableResponse<Response> 신분당선 = createLineRequest("신분당선", "bg-red-600", firstStationId, secondStationId, 10);
        assertThat(신분당선.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // When
        long lineId = 신분당선.jsonPath().getLong("id");

        Map<String, String> lineChangeRequest = new HashMap<>();
        lineChangeRequest.put("name", "다른분당선");
        lineChangeRequest.put("color", "bg-red-600");

        ExtractableResponse<Response> 지하철노선_수정_응답 = RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .body(lineChangeRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{id}")
                .then().log().all()
                .extract();
        assertThat(지하철노선_수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // Then
        ExtractableResponse<Response> 지하철노선_특정_조회_응답 = findLineByIdAPIResponse(lineId);

        assertAll(
                () -> assertThat(지하철노선_특정_조회_응답.jsonPath().getString("name")).isEqualTo("다른분당선"),
                () -> assertThat(지하철노선_특정_조회_응답.jsonPath().getString("color")).isEqualTo("bg-red-600")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        // Given
        String 첫번째역 = "첫번째역";
        String 두번째역 = "새로운지하철역";

        long firstStationId = createStationRequest(첫번째역);
        long secondStationId = createStationRequest(두번째역);

        ExtractableResponse<Response> 신분당선 = createLineRequest("신분당선", "bg-red-600", firstStationId, secondStationId, 10);
        assertThat(신분당선.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // When
        long lineId = 신분당선.jsonPath().getLong("id");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<Long> lineIds = findAllLinesRequest().jsonPath().getList("id", Long.class);
        assertThat(lineIds).hasSize(0);
    }
    private long createStationRequest(String stationName) {
        Map<String, String> station = new HashMap<>();
        station.put("name", stationName);

        ExtractableResponse<Response> createStationResponse = RestAssured
                .given().log().all()
                .body(station)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(createStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createStationResponse.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> createLineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        lineRequest.put("upStationId", upStationId);
        lineRequest.put("downStationId", downStationId);
        lineRequest.put("distance", distance);

        ExtractableResponse<Response> createLineResponse = RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createLineResponse;
    }

    private ExtractableResponse<Response> findAllLinesRequest() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findLineByIdAPIResponse(long lineId) {
        return RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }
}
