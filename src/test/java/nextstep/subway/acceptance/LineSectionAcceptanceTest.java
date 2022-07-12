package nextstep.subway.acceptance;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.LineAcceptanceTest.createLine;
import static nextstep.subway.acceptance.LineAcceptanceTest.getLine;
import static nextstep.subway.acceptance.StationAcceptanceTest.NAME;
import static nextstep.subway.acceptance.StationAcceptanceTest.createStation;
import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철구간 관련 기능")
public class LineSectionAcceptanceTest extends BaseAcceptanceTest {


    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        createStation(Map.of(NAME, "남태령역"));
        createStation(Map.of(NAME, "사당역"));
        createStation(Map.of(NAME, "총신대입구역"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 노선의 하행 종점역에 등록된 역을 상행 종점역으로 생성하고, 하행 종점역은 신규 역으로 지정하면
     * Then 노선에 구간이 추가된다.
     */
    @Test
    @DisplayName("지하철 노선에 구간이 추가된다.")
    void createSectionTest() {
        // given
        long lineId = createLineAndGetId(1, 2);

        // when
        ExtractableResponse<Response> response = createSection(lineId, 2, 3, 10);
        checkResponseStatus(response, HttpStatus.CREATED);

        //then
        ExtractableResponse<Response> lineResponse = getLine(lineId);
        assertThat(lineResponse.jsonPath().getList("stations")).hasSize(3);
        assertThat(lineResponse.jsonPath().getList("stations")).containsAnyOf("총신대입구역");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 노선의 하행 종점역에 등록되지 않은 역을 상행 종점역으로 생성하면
     * Then 노선에 구간 추가가 실패한다.
     */
    @Test
    @DisplayName("지하철 노선에 하행 종점역이 아닌 역을 상행 종점역으로 설정하면 구간 추가가 실패한다.")
    void createSectionFailTest() {
        // given
        long lineId = createLineAndGetId(1, 2);

        // when
        ExtractableResponse<Response> response = createSection(lineId, 1, 3, 10);
        checkResponseStatus(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 노선에 등록된 역을 상행 또는 하행 종점역으로 생성하면
     * Then 노선에 구간 추가가 실패한다.
     */
    @Test
    @DisplayName("지하철 노선에 존재하는 구간을 추가하면 구간 추가가 실패한다.")
    void createSectionExistStationFailTest() {
        // given
        long lineId = createLineAndGetId(1, 2);

        // when
        ExtractableResponse<Response> response = createSection(lineId, 2, 1, 10);
        checkResponseStatus(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * Given 지하철 노선을 생성하고
     * Given 구간을 추가로 생성한다음
     * When 노선에 등록된 마지막 역을 제거하면
     * Then 노선에 구간 제거가 성공한다.
     */
    @Test
    @DisplayName("지하철 노선에 구간 제거가 성공한다.")
    void deleteSelectionTest() {
        throw new RuntimeException("todo");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 노선에 등록된 마지막 역을 제거하면
     * Then 노선에 구간 제거에 실패한다 (구간이 1개)
     */
    @Test
    @DisplayName("지하철 노선에 구간 제거가 실패한다.")
    void deleteSelectionFailTest() {
        throw new RuntimeException("todo");
    }

    private long createLineAndGetId(long upStationId, long downStationId) {
        return createLine("4호선", "bg-blue-300", upStationId, downStationId, 10)
                .jsonPath()
                .getLong("id");
    }

    private ExtractableResponse<Response> createSection(long lineId, long upStationId, long downStationId, int distance) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of(
                        "upStationId", upStationId,
                        "downStationId", downStationId,
                        "distance", distance
                ))
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

}
