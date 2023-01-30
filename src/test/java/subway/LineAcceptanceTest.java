package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.route.LineRequest;
import subway.route.LineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private static final String BASE_URL = "/lines";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COLOR = "color";
    private static final String STATIONS = "stations";


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

    private static Long 지하철역생성후ID반환(String gangnam) {
        ExtractableResponse<Response> 강남역 = StationAcceptanceTest.createSubwayStation(gangnam);
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
}
