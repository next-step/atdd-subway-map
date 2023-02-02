package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import subway.common.AcceptanceTestTruncateListener;
import subway.common.AssertUtil;
import subway.exception.LineNotFoundException;
import subway.station.StationAcceptanceTest;
import subway.station.StationConstant;
import subway.station.StationStep;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineConstant.*;
import static subway.line.LineStep.*;
import static subway.station.StationConstant.*;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestExecutionListeners(value = {AcceptanceTestTruncateListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class LineAcceptanceTest {

    private Long upStationId;
    private Long downStationId;

    @BeforeEach
    void setUp() {
        upStationId = 지하철역생성후_식별번호_반환(GANGNAM);
        downStationId = 지하철역생성후_식별번호_반환(YANGJAE);
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
        long id = extractId(response);

        // then
        AssertUtil.상태코드_CREATED(response);

        List<LineResponse> lines = getAllLines();
        노선목록값_검증(id, COLOR_VALUE1, List.of(GANGNAM, YANGJAE), 1, lines);
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
        Long upStationId2 = 지하철역생성후_식별번호_반환(HAGYE);
        Long downStationId2 = 지하철역생성후_식별번호_반환(JUNGGYE);

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
                GANGNAM,
                YANGJAE,
                HAGYE,
                JUNGGYE
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
        long id = extractId(createLineResponse);

        // when
        ExtractableResponse<Response> response = getLine(id);

        // then
        AssertUtil.상태코드_OK(response);
        노선응답값검증(response, id, NAME_VALUE1, COLOR_VALUE1, GANGNAM, YANGJAE);
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
        long id = extractId(createLineResponse);

        // when
        ExtractableResponse<Response> updateResponse = updateLine(id, NAME_VALUE2, COLOR_VALUE2);

        // then
        AssertUtil.상태코드_OK(updateResponse);
        ExtractableResponse<Response> response = getLine(id);
        노선응답값검증(response, id, NAME_VALUE2, COLOR_VALUE2, GANGNAM, YANGJAE);
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
        long id = extractId(createLineResponse);

        // when
        ExtractableResponse<Response> deleteResponse = deleteLine(id);

        // then
        AssertUtil.상태코드_NO_CONTENT(deleteResponse);
        AssertUtil.상태코드_NOT_FOUND(getLine(id));
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
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(deleteResponse.jsonPath().getString("message")).isEqualTo(LineNotFoundException.EXCEPTION_MESSAGE);
    }

    private static Long 지하철역생성후_식별번호_반환(String stationName) {
        ExtractableResponse<Response> response = StationStep.createSubwayStation(stationName);
        return StationStep.extractStationId(response);
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

    private static void 노선목록값_검증(long id, String color, List<String> stationNames, int size, List<LineResponse> lines) {
        assertThat(lines).hasSize(1);
        assertThat(lines).extracting(ID).containsExactly(id);
        assertThat(lines).extracting(COLOR).containsExactly(COLOR_VALUE1);
        assertThat(lines).flatExtracting(STATIONS).extracting(NAME)
                .contains(stationNames.toArray(new String[0]));
    }
}
