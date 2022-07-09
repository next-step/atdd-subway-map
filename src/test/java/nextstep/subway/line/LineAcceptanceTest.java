package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestBase.assertStatusCode;
import static nextstep.subway.station.StationAcceptanceTest.createStationRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@AcceptanceTest
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    private Long upStationId;
    private Long downStationId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        upStationId = createStation("강남역");
        downStationId = createStation("양재역");
    }

    private Long createStation(final String stationName) {
        return createStationRequest(stationName).jsonPath().getObject("id", Long.class);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        final String lineName = "신분당선";

        // when
        final ExtractableResponse<Response> createLineResponse = createLineRequest(lineName);

        // then
        assertStatusCode(createLineResponse, HttpStatus.CREATED);

        final List<Long> stationIdList = createLineResponse.jsonPath().getList("stations.id", Long.class);
        assertThat(stationIdList).contains(upStationId, downStationId);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLines() {
        // given
        final String lineName1 = "신분당선";
        createLineRequest(lineName1);

        final String lineName2 = "2호선";
        createLineRequest(lineName2);

        // when
        final ExtractableResponse<Response> response = getLinesRequest();

        // then
        assertStatusCode(response, HttpStatus.OK);

        final List<String> names = getLineNames(response);
        assertThat(names).hasSize(2);
        assertThat(names).contains(lineName1, lineName2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void modifyLines() {
        // given
        final String beforeLineName = "신분당선";
        final Long id = createLineRequest(beforeLineName).jsonPath().getObject("id", Long.class);

        final String afterLineName = "신신분당선";

        // when
        final ExtractableResponse<Response> modifyResponse = modifyLineRequest(id, afterLineName);

        // then
        assertStatusCode(modifyResponse, HttpStatus.OK);

        final List<String> names = getLineNames(getLinesRequest());
        assertThat(names).contains(afterLineName);
        assertThat(names).doesNotContain(beforeLineName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        final String lineName = "신분당선";
        final Long id = createLineRequest(lineName).jsonPath().getObject("id", Long.class);

        // when
        final ExtractableResponse<Response> response = deleteLineRequest(id);

        // then
        assertStatusCode(response, HttpStatus.OK);

        final List<String> names = getLineNames(getLinesRequest());
        assertThat(names).doesNotContain(lineName);
    }

    private ExtractableResponse<Response> modifyLineRequest(final Long id, final String lineName) {
        return RestAssured
                .given().log().all().body(createLineParams(lineName)).contentType(ContentType.JSON)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createLineRequest(final String lineName) {
        return RestAssured
                .given().log().all()
                .body(createLineParams(lineName))
                .contentType(ContentType.JSON)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private Map<String, Object> createLineParams(final String lineName) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);
        return params;
    }

    private ExtractableResponse<Response> getLinesRequest() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private List<String> getLineNames(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> deleteLineRequest(final Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }
}
