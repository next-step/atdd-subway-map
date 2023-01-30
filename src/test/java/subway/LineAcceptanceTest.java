package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineRequest;
import subway.line.LineResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COLOR = "color";
    private static final String STATIONS = "stations";

    private static final String BASE_URL = "/lines";
    private static final String DETAIL_URL = "/lines/{" + ID + "}";

    private static final String NAME_VALUE1 = "신분당선";
    private static final String COLOR_VALUE1 = "bg-red-600";
    private static final String NAME_VALUE2 = "7호선";
    private static final String COLOR_VALUE2 = "bg-green-300";

    private static final Long DISTANCE_VALUE = 10L;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성합니다.")
    @Test
    void createLineTest() {
        // when
        Long upStationId = 지하철역생성후ID반환(StationAcceptanceTest.GANGNAM);
        Long downStationId = 지하철역생성후ID반환(StationAcceptanceTest.YANGJAE);
        ExtractableResponse<Response> response = createLineWithLineRequest(NAME_VALUE1, COLOR_VALUE1, upStationId, downStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        long id = response.jsonPath().getLong(ID);

        List<LineResponse> lines = getAllLines();
        assertThat(lines).hasSize(1);
        assertThat(lines).extracting(ID).containsExactly(id);
        assertThat(lines).extracting(COLOR).containsExactly(COLOR_VALUE1);
        assertThat(lines).flatExtracting(STATIONS).extracting(NAME)
                .contains(StationAcceptanceTest.GANGNAM, StationAcceptanceTest.YANGJAE);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        Long upStationId = 지하철역생성후ID반환(StationAcceptanceTest.GANGNAM);
        Long downStationId = 지하철역생성후ID반환(StationAcceptanceTest.YANGJAE);
        Long upStationId2 = 지하철역생성후ID반환(StationAcceptanceTest.HAGYE);
        Long downStationId2 = 지하철역생성후ID반환(StationAcceptanceTest.JUNGGYE);

        // when
        LineRequest lineRequest = new LineRequest(NAME_VALUE1, COLOR_VALUE1, upStationId, downStationId, DISTANCE_VALUE);
        LineRequest lineRequest2 = new LineRequest(NAME_VALUE2, COLOR_VALUE2, upStationId2, downStationId2, DISTANCE_VALUE);
        createLine(lineRequest);
        createLine(lineRequest2);

        List<LineResponse> lines = getAllLines();

        // then
        assertThat(lines).extracting(NAME).contains(
                NAME_VALUE1,
                NAME_VALUE2
        );
        assertThat(lines).extracting(COLOR).contains(
                COLOR_VALUE1,
                COLOR_VALUE2
        );
        assertThat(lines).flatExtracting(STATIONS).extracting(NAME).contains(
                StationAcceptanceTest.GANGNAM,
                StationAcceptanceTest.YANGJAE,
                StationAcceptanceTest.HAGYE,
                StationAcceptanceTest.JUNGGYE
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회합니다.")
    @Test
    void getLineTest() {
        //given
        Long upStationId = 지하철역생성후ID반환(StationAcceptanceTest.GANGNAM);
        Long downStationId = 지하철역생성후ID반환(StationAcceptanceTest.YANGJAE);
        ExtractableResponse<Response> createLineResponse = createLineWithLineRequest(NAME_VALUE1, COLOR_VALUE1, upStationId, downStationId);
        long id = createLineResponse.jsonPath().getLong(ID);

        // when
        ExtractableResponse<Response> response = getLine(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        노선응답값검증(response, id, NAME_VALUE1, COLOR_VALUE1, StationAcceptanceTest.GANGNAM, StationAcceptanceTest.YANGJAE);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정합니다.")
    @Test
    void updateLineTest() {
        // given
        Long upStationId = 지하철역생성후ID반환(StationAcceptanceTest.GANGNAM);
        Long downStationId = 지하철역생성후ID반환(StationAcceptanceTest.YANGJAE);
        Long changeUpStationId = 지하철역생성후ID반환(StationAcceptanceTest.HAGYE);
        Long changeDownStationId = 지하철역생성후ID반환(StationAcceptanceTest.JUNGGYE);
        ExtractableResponse<Response> createLineResponse = createLineWithLineRequest(NAME_VALUE1, COLOR_VALUE1, upStationId, downStationId);
        long id = createLineResponse.jsonPath().getLong(ID);

        // when
        ExtractableResponse<Response> updateResponse = updateLine(id, NAME_VALUE2, COLOR_VALUE2, changeUpStationId, changeDownStationId, DISTANCE_VALUE + DISTANCE_VALUE);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = getLine(id);
        노선응답값검증(response, id, NAME_VALUE2, COLOR_VALUE2, StationAcceptanceTest.HAGYE, StationAcceptanceTest.JUNGGYE);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제합니다.")
    @Test
    void deleteLineTest() {
        // given
        Long upStationId = 지하철역생성후ID반환(StationAcceptanceTest.GANGNAM);
        Long downStationId = 지하철역생성후ID반환(StationAcceptanceTest.YANGJAE);
        ExtractableResponse<Response> createLineResponse = createLineWithLineRequest(NAME_VALUE1, COLOR_VALUE1, upStationId, downStationId);
        long id = createLineResponse.jsonPath().getLong(ID);

        // when
        ExtractableResponse<Response> deleteResponse = deleteLine(id);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(getLine(id).statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> deleteLine(long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(DETAIL_URL, Map.of(ID, id))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> updateLine(long id, String name, String color, Long upStationId, Long downStationId, Long distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().put(DETAIL_URL, Map.of(ID, id))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getLine(long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(DETAIL_URL, Map.of(ID, id))
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> createLineWithLineRequest(String name, String color, Long upStationId, Long downStationId) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, DISTANCE_VALUE);
        ExtractableResponse<Response> response = createLine(lineRequest);
        return response;
    }

    private static ExtractableResponse<Response> createLine(LineRequest lineRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(BASE_URL)
                .then().log().all()
                .extract();
        return response;
    }

    private static Long 지하철역생성후ID반환(String stationName) {
        ExtractableResponse<Response> 강남역 = StationAcceptanceTest.createSubwayStation(stationName);
        Long upStationId = StationAcceptanceTest.extractStationId(강남역);
        return upStationId;
    }

    private static List<LineResponse> getAllLines() {
        ExtractableResponse<Response> allLines = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(BASE_URL)
                .then().log().all()
                .extract();
        return allLines.jsonPath().getList("", LineResponse.class);
    }

    private static void 노선응답값검증(ExtractableResponse<Response> response, long id, String nameValue1, String colorValue1, String... stations) {
        LineResponse lineResponse = response.body().as(LineResponse.class);
        assertThat(lineResponse.getId()).isEqualTo(id);
        assertThat(lineResponse.getName()).isEqualTo(nameValue1);
        assertThat(lineResponse.getColor()).isEqualTo(colorValue1);
        assertThat(lineResponse.getStations())
                .extracting(NAME).contains(stations);
    }
}
