package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestBase.assertStatusCode;
import static nextstep.subway.acceptance.ResponseParser.*;
import static nextstep.subway.station.StationAcceptanceTest.createStationRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@AcceptanceTest
public class LineAcceptanceTest {

    private Long upStationId;
    private Long downStationId;

    @BeforeEach
    void setUp() {
        upStationId = createStation("강남역");
        downStationId = createStation("양재역");
    }

    private Long createStation(final String stationName) {
        return getIdFromResponse(createStationRequest(stationName));
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        final String 신분당선 = "신분당선";

        // when
        final ExtractableResponse<Response> createLineResponse = createLineRequest(신분당선);

        // then
        assertStatusCode(createLineResponse, HttpStatus.CREATED);
        assertThat(getNamesFromResponse(getLinesRequest())).contains(신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final String 신분당선 = "신분당선";
        final Long id = getIdFromResponse(createLineRequest(신분당선));

        // when
        final ExtractableResponse<Response> response = getLineRequest(id);

        // then
        assertStatusCode(response, HttpStatus.OK);

        assertThat(getNameFromResponse(response)).contains(신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */

    @DisplayName("지하철 노선 목을 조회한다.")
    @Test
    void getLines() {
        // given
        final String 신분당선 = "신분당선";
        createLineRequest(신분당선);

        final String 신신분당선 = "신신분당선";
        createLineRequest(신신분당선);

        // when
        final ExtractableResponse<Response> response = getLinesRequest();

        // then
        assertStatusCode(response, HttpStatus.OK);

        final List<String> lineNames = getNamesFromResponse(response);
        assertThat(lineNames).containsExactly(신분당선, 신신분당선);
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
        final String 신분당선 = "신분당선";
        final Long id = getIdFromResponse(createLineRequest(신분당선));

        final String 신신분당선 = "신신분당선";

        // when
        final ExtractableResponse<Response> modifyResponse = modifyLineRequest(id, 신신분당선);

        // then
        assertStatusCode(modifyResponse, HttpStatus.OK);

        final List<String> lineNames = getNamesFromResponse(getLinesRequest());
        assertThat(lineNames).contains(신신분당선);
        assertThat(lineNames).doesNotContain(신분당선);
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
        final String 신분당선 = "신분당선";
        final Long id = getIdFromResponse(createLineRequest(신분당선));

        // when
        final ExtractableResponse<Response> response = deleteLineRequest(id);

        // then
        assertStatusCode(response, HttpStatus.NO_CONTENT);

        final List<String> lineNames = getNamesFromResponse(getLinesRequest());
        assertThat(lineNames).doesNotContain(신분당선);
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

    public static ExtractableResponse<Response> getLineRequest(final Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/{idd}", id)
                .then().log().all()
                .extract();
    }


    private ExtractableResponse<Response> getLinesRequest() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteLineRequest(final Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> createLineRequest(final Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static Map<String, Object> createLineParams(
            final String lineName,
            final Long upStationId,
            final Long downStationId) {

        final Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);
        return params;
    }
}
