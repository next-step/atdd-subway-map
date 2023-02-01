package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hibernate.annotations.SQLDelete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import subway.exception.LineNotFoundException;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestExecutionListeners(value = {AcceptanceTestTruncateListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
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

    private Long upStationId;
    private Long downStationId;

    @BeforeEach
    void setUp() {
        upStationId = 지하철역생성후_식별번호_반환(StationAcceptanceTest.GANGNAM);
        downStationId = 지하철역생성후_식별번호_반환(StationAcceptanceTest.YANGJAE);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성합니다.")
    @Test
    void createLineTest() {
        // when
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
        Long upStationId2 = 지하철역생성후_식별번호_반환(StationAcceptanceTest.HAGYE);
        Long downStationId2 = 지하철역생성후_식별번호_반환(StationAcceptanceTest.JUNGGYE);

        // when
        LineCreateRequest lineRequest = new LineCreateRequest(NAME_VALUE1, COLOR_VALUE1, upStationId, downStationId, DISTANCE_VALUE);
        LineCreateRequest lineRequest2 = new LineCreateRequest(NAME_VALUE2, COLOR_VALUE2, upStationId2, downStationId2, DISTANCE_VALUE);
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
        ExtractableResponse<Response> createLineResponse = createLineWithLineRequest(NAME_VALUE1, COLOR_VALUE1, upStationId, downStationId);
        long id = createLineResponse.jsonPath().getLong(ID);

        // when
        ExtractableResponse<Response> updateResponse = updateLine(id, NAME_VALUE2, COLOR_VALUE2);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = getLine(id);
        노선응답값검증(response, id, NAME_VALUE2, COLOR_VALUE2, StationAcceptanceTest.GANGNAM, StationAcceptanceTest.YANGJAE);
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
        ExtractableResponse<Response> createLineResponse = createLineWithLineRequest(NAME_VALUE1, COLOR_VALUE1, upStationId, downStationId);
        long id = createLineResponse.jsonPath().getLong(ID);

        // when
        ExtractableResponse<Response> deleteResponse = deleteLine(id);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(getLine(id).statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * When 지하철 생성없이 지하철을 삭제하면
     * Then 500에러와 함께 예외메시지 응답이온다.
     */
    @DisplayName("지하철 노선을 삭제 시 지하철 노선이 존재하지 않는다면 예외가 발생합니다.")
    @Test
    void deleteLineIfNotExistException() {
        // when
        ExtractableResponse<Response> deleteResponse = deleteLine(1L);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(deleteResponse.jsonPath().getString("message")).isEqualTo(LineNotFoundException.EXCEPTION_MESSAGE);
    }

    private ExtractableResponse<Response> deleteLine(long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(DETAIL_URL, Map.of(ID, id))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> updateLine(long id, String name, String color) {
        LineUpdateRequest lineRequest = new LineUpdateRequest(name, color);
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
        LineCreateRequest lineRequest = new LineCreateRequest(name, color, upStationId, downStationId, DISTANCE_VALUE);
        ExtractableResponse<Response> response = createLine(lineRequest);
        return response;
    }

    private static ExtractableResponse<Response> createLine(LineCreateRequest lineRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(BASE_URL)
                .then().log().all()
                .extract();
        return response;
    }

    private static Long 지하철역생성후_식별번호_반환(String stationName) {
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

    private static void 노선응답값검증(ExtractableResponse<Response> response, long id, String name, String color, String... stationNames) {
        LineResponse lineResponse = response.body().as(LineResponse.class);
        assertThat(lineResponse.getId()).isEqualTo(id);
        assertThat(lineResponse.getName()).isEqualTo(name);
        assertThat(lineResponse.getColor()).isEqualTo(color);
        assertThat(lineResponse.getStations())
                .extracting(NAME, String.class).contains(stationNames);
    }
}
