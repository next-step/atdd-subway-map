package subway;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.dto.LineRequest;
import subway.line.dto.SectionRequest;
import subway.station.dto.StationRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends BaseTest{

    private static final String LINE_API_PATH = "/lines";
    private static final String SECTION_API_PATH = "/lines/{id}/sections";
    private static final String STATION_API_PATH = "/stations";

    private Long 강남역_ID;
    private Long 역삼역_ID;
    private Long 지하철역_ID;
    private Long 신분당선_ID;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        final StationRequest 강남역_생성_요청 = new StationRequest("강남역");
        final ExtractableResponse<Response> 강남역_생성_응답 = callPostApi(강남역_생성_요청, STATION_API_PATH);
        강남역_ID = getIdFromApiResponse(강남역_생성_응답);

        final StationRequest 역삼역_생성_요청 = new StationRequest("역삼역");
        final ExtractableResponse<Response> 역삼역_생성_응답 = callPostApi(역삼역_생성_요청, STATION_API_PATH);
        역삼역_ID = getIdFromApiResponse(역삼역_생성_응답);

        final StationRequest 지하역_생성_요청 = new StationRequest("지하철역");
        final ExtractableResponse<Response> 지하철역_생성_응답 = callPostApi(지하역_생성_요청, STATION_API_PATH);
        지하철역_ID = getIdFromApiResponse(지하철역_생성_응답);
    }

    /**
     * Given 1개의 지하철 노선을 등록하고
     * When 지하철 노선에 구간을 등록하면
     * Then 지하철 노선 조회 시 새로운 구간의 하행역을 찾을 수 있다
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void 지하철_구간_등록() {
        // given
        this.신분당선_생성();

        // when
        final JsonPath createSectionResponse = this.신분당선_구간_연장_역삼역_지하철역();

        // then
        final String lineName = createSectionResponse.get("name");
        assertThat(lineName).isEqualTo("신분당선");

        final String newDownStationName = createSectionResponse.get("stations[2].name");
        assertThat(newDownStationName).isEqualTo("지하철역");
    }

    /**
     * Given 지하철 구간을 생성하고
     * When 생성한 지하철 구간을 삭제하면
     * Then 해당 지하철 구간 정보는 삭제된다
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void 지하철_구간_삭제() {
        // given
        this.신분당선_생성();
        this.신분당선_구간_연장_역삼역_지하철역();

        // when
        callDeleteApi("stationId", 지하철역_ID, SECTION_API_PATH, 신분당선_ID);

        // then
        ExtractableResponse<Response> 신분당선_조회_요청 = callGetApi(LINE_API_PATH + "/{id}", 신분당선_ID);
        final JsonPath getLineResponse = 신분당선_조회_요청.jsonPath();

        List<String> names = getLineResponse.get("stations.name");
        assertThat(names).containsOnly("강남역", "역삼역");
    }

    private JsonPath 신분당선_구간_연장_역삼역_지하철역() {
        final SectionRequest sectionRequest = new SectionRequest(역삼역_ID, 지하철역_ID, 5);
        final ExtractableResponse<Response> createSectionResponse = callPostApi(sectionRequest, SECTION_API_PATH, 신분당선_ID);
        final JsonPath jsonPath = createSectionResponse.jsonPath();

        return jsonPath;
    }

    private void 신분당선_생성() {
        final LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
        final ExtractableResponse<Response> createLineResponse = callPostApi(lineRequest, LINE_API_PATH);
        신분당선_ID = getIdFromApiResponse(createLineResponse);
    }

}
