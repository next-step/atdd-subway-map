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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
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
        // Given
        String 첫번째역 = "지하철역";
        String 두번째역 = "새로운지하철역";

        ExtractableResponse<Response> 첫번째역_생성_응답 = createStationRequest(첫번째역);
        ExtractableResponse<Response> 두번째역_생성_응답 = createStationRequest(두번째역);

        assertAll(
                () -> assertThat(첫번째역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(두번째역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        );

        // When
        String lineName = "신분당선";
        String lineColor = "bg-red-600";
        Long upStationId = 1L;
        Long downStationId = 2L;
        int distance = 10;

        ExtractableResponse<Response> 첫번째노선_생성_응답 = createLineRequest(lineName, lineColor, upStationId, downStationId, distance);

        // Then
        List<String> stationNames = 첫번째노선_생성_응답.jsonPath().getList("stations.name", String.class);

        assertAll(
                () -> assertThat(첫번째노선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(stationNames).containsAnyOf("지하철역", "새로운지하철역")
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

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void getLineById() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {

    }

    private ExtractableResponse<Response> createStationRequest(String stationName) {
        Map<String, String> station = new HashMap<>();
        station.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(station)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createLineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        LineRequest line = new LineRequest(name, color, upStationId, downStationId, distance);

        return RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

}
