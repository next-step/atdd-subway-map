package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
        ExtractableResponse<Response> 신분당선 = 노선_생성("신분당선", "bg-red-600", "강남역", "신논현역", 10);

        // Then
        ExtractableResponse<Response> 모든_노선_조회_응답 = findAllLinesRequest();
        List<String> 지하철역_이름 = 모든_노선_조회_응답.jsonPath().getList("stations[0].name", String.class);

        assertAll(
                () -> assertThat(지하철역_이름).hasSize(2),
                () -> assertThat(지하철역_이름).containsAnyOf("강남역", "신논현역")
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
        ExtractableResponse<Response> 신분당선 = 노선_생성("신분당선", "bg-red-600", "강남역", "정자역", 10);
        ExtractableResponse<Response> 분당선 = 노선_생성("분당선", "bg-yellow-600", "정자역", "이매역", 10);

        // When
        ExtractableResponse<Response> 모든_노선_조회_응답 = findAllLinesRequest();
        List<String> 신분당선_역이름 = 모든_노선_조회_응답.jsonPath().get("stations[0].name");
        List<String> 분당선_역이름 = 모든_노선_조회_응답.jsonPath().get("stations[1].name");

        // Then
        assertAll(
                () -> assertThat(신분당선_역이름).hasSize(2),
                () -> assertThat(신분당선_역이름).containsAnyOf("강남역", "정자역"),
                () -> assertThat(분당선_역이름).hasSize(2),
                () -> assertThat(분당선_역이름).containsAnyOf("정자역", "이매역")
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
        ExtractableResponse<Response> 신분당선 = 노선_생성("신분당선", "bg-red-600", "강남역", "정자역", 10);

        // When
        long lineId = 신분당선.jsonPath().getLong("id");
        ExtractableResponse<Response> 신분당선_조회_응답 = findLineByIdAPIResponse(lineId);
        List<String> 지하철역_이름 = 신분당선_조회_응답.jsonPath().getList("stations.name", String.class);

        // Then
        assertAll(
                () -> assertThat(지하철역_이름).hasSize(2),
                () -> assertThat(지하철역_이름).containsAnyOf("강남역", "신논현역")
        );
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
        ExtractableResponse<Response> 신분당선 = 노선_생성("신분당선", "bg-red-600", "강남역", "정자역", 10);

        // When
        long lineId = 신분당선.jsonPath().getLong("id");
        ExtractableResponse<Response> 신분당선_수정_응답 = changeLineRequest(lineId, "새로운분당선", "bg-red-700");

        // Then
        ExtractableResponse<Response> 지하철노선_특정_조회_응답 = findLineByIdAPIResponse(lineId);
        String changedName = 지하철노선_특정_조회_응답.jsonPath().getString("name");
        String changedColor = 지하철노선_특정_조회_응답.jsonPath().getString("color");

        assertAll(
                () -> assertThat(changedName).isEqualTo("새로운분당선"),
                () -> assertThat(changedColor).isEqualTo("bg-red-700")
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
        ExtractableResponse<Response> 신분당선 = 노선_생성("신분당선", "bg-red-600", "강남역", "정자역", 10);

        // When
        long lineId = 신분당선.jsonPath().getLong("id");
        ExtractableResponse<Response> 신분당선_삭제_응답 = deleteLineRequest(lineId);

        // Then
        ExtractableResponse<Response> 모든_노선_조회_응답 = findAllLinesRequest();
        List<Long> lineIds = findAllLinesRequest().jsonPath().getList("id", Long.class);
        assertThat(lineIds).hasSize(0);
    }

    private ExtractableResponse<Response> 노선_생성(String name, String color, String upStation, String downStation, int distance) {
        long upStationId = createStationRequest(upStation).jsonPath().getLong("id");
        long downStationId = createStationRequest(downStation).jsonPath().getLong("id");

        ExtractableResponse<Response> 노선_조회_응답 = createLineRequest(name, color, upStationId, downStationId, distance);

        assertThat(노선_조회_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return 노선_조회_응답;
    }

    private ExtractableResponse<Response> createStationRequest(String stationName) {
        // Given
        Map<String, String> stationRequest = new HashMap<>();
        stationRequest.put("name", stationName);

        // When
        ExtractableResponse<Response> createStationResponse = RestAssured
                .given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        // Then
        assertThat(createStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createStationResponse;
    }

    private ExtractableResponse<Response> createLineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        // Given
        Map<String, Object> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        lineRequest.put("upStationId", upStationId);
        lineRequest.put("downStationId", downStationId);
        lineRequest.put("distance", distance);

        // When
        ExtractableResponse<Response> createLineResponse = RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // Then
        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createLineResponse;
    }

    private ExtractableResponse<Response> findAllLinesRequest() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private ExtractableResponse<Response> findLineByIdAPIResponse(long lineId) {
        return RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> changeLineRequest(long lineId, String name, String color) {
        Map<String, String> lineChangeRequest = new HashMap<>();
        lineChangeRequest.put("name", name);
        lineChangeRequest.put("color", color);

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

        return 지하철노선_수정_응답;
    }

    private ExtractableResponse<Response> deleteLineRequest(long lineId) {
        ExtractableResponse<Response> 지하철노선_삭제_응답 = RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
        assertThat(지하철노선_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        return 지하철노선_삭제_응답;
    }
}
