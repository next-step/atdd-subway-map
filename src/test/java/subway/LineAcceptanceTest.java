package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.StationResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationAcceptanceTest.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static final String LINE_PATH = "/lines";
    private static final String LOCATION = "Location";
    private static final String URI_DELIMITER = "/";
    private static final String LINE_ID = "id";
    private static final int LINE_ID_INDEX = 2;


    private StationResponse 강남역;
    private StationResponse 판교역;

    private Map<String, String> createLineParams;

    @BeforeEach
    void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        판교역 = 지하철역_생성_요청("판교역").as(StationResponse.class);

        createLineParams = getLineParams("신분당선", "red", 강남역.getId(), 판교역.getId(), 14);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(createLineParams);

        // then
        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = 지하철_노선_목록_조회_요청().jsonPath().getList("name", String.class);
        List<String> stationNames = 지하철_노선_목록_조회_요청().jsonPath().getList("stations.name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
        assertThat(stationNames).containsAll(List.of("강남역", "판교역"));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        StationResponse 정자역 = 지하철역_생성_요청("정자역").as(StationResponse.class);
        Map<String, String> lineCreateParams1 = getLineParams("수인분당선", "yellow", 판교역.getId(), 정자역.getId(), 3);

        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_생성_요청(createLineParams);
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_생성_요청(lineCreateParams1);
        List<Long> expectedStationIds = getCreateLineIds(Arrays.asList(createLineResponse1, createLineResponse2));

        // when
        ExtractableResponse<Response> getLineResponse = 지하철_노선_목록_조회_요청();
        List<Long> lineIds = getLineResponse.jsonPath().getList(LINE_ID, Long.class);

        // then
        assertThat(getLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineIds).containsAll(expectedStationIds);
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
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(createLineParams);
        Long expectedLineId = getCreateLineId(createLineResponse);

        // when
        ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(createLineResponse);
        Long lineId = getLineResponse.jsonPath().get(LINE_ID);

        // then
        assertThat(getLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineId).isEqualTo(expectedLineId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보를 조회시 노선 정보가 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(createLineParams);

        // when
        Map<String, String> updateLineParams = new HashMap<>();
        updateLineParams.put("name", "수인분당선");
        updateLineParams.put("color", "yellow");
        ExtractableResponse<Response> updateLineResponse = 지하철_노선_수정_요청(createLineResponse, updateLineParams);

        ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(createLineResponse);
        String lineName = getLineResponse.jsonPath().get("name");
        String lineColor = getLineResponse.jsonPath().get("color");

        // then
        assertThat(updateLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo("수인분당선");
        assertThat(lineColor).isEqualTo("yellow");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 지하철 노선 목록 조회시 해당 지하철 노선 정보는 조회할 수 없다.
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(createLineParams);

        // when
        ExtractableResponse<Response> deleteLineResponse = 지하철_노선_삭제_요청(createLineResponse);

        // then
        List<String> lineNames = 지하철_노선_목록_조회_요청().jsonPath().getList("name", String.class);
        assertThat(deleteLineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(lineNames).doesNotContain("신분당선").isEmpty();
    }

    private List<Long> getCreateLineIds(List<ExtractableResponse<Response>> createLineResponses) {
        return createLineResponses.stream()
                .map(this::getCreateLineId)
                .collect(Collectors.toList());
    }

    private Long getCreateLineId(ExtractableResponse<Response> createLineResponse) {
        return Long.parseLong(createLineResponse.header(LOCATION).split(URI_DELIMITER)[LINE_ID_INDEX]);
    }

    private Map<String, String> getLineParams(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", String.valueOf(upStationId));
        lineCreateParams.put("downStationId", String.valueOf(downStationId));
        lineCreateParams.put("distance", String.valueOf(distance));
        return lineCreateParams;
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(LINE_PATH)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(LINE_PATH)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createLineResponse) {
        String uri = createLineResponse.header(LOCATION);
        return RestAssured.given().log().all()
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> createLineResponse, Map<String, String> params) {
        String uri = createLineResponse.header(LOCATION);
        return RestAssured.given().log().all()
                .body(params)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_삭제_요청(ExtractableResponse<Response> createLineResponse) {
        String uri = createLineResponse.header(LOCATION);
        return RestAssured.given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }
}
