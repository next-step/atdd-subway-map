package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    public static final String[] CLEAN_UP_TABLES = {"subway_line", "station", "section"};

    private final SubwayCallApi subwayCallApi;

    public SectionAcceptanceTest() {
        this.subwayCallApi = new SubwayCallApi();
    }

    @Override
    protected void preprocessing() {
        cleanUpSchema.execute(CLEAN_UP_TABLES);

        subwayCallApi.saveStation(Param.강남역);
        subwayCallApi.saveStation(Param.양재역);
        subwayCallApi.saveStation(Param.양재시민의숲역);
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 지하철 구간을 생성하면
     * Then 지하철 노선 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createSubwaySection() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = ActualUtils.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveSectionResponse = subwayCallApi.saveSection(lineId, Param.신분당선_구간);

        // then
        assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> response = subwayCallApi.findSubwayLineById(lineId);
        assertThat(ActualUtils.getList(response, "stations.name", String.class)).isEqualTo(List.of("강남역","양재역","양재시민의숲역"));
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 하행 종점역에 등록되지 않은 상행역을 등록하면
     * Then 예외가 발생한다.
     */
    @DisplayName("상행역 등록 시 하행 종점역에 등록되지 않은 역인 경우 예외")
    @Test
    void given_createSubwaySection_when_upStationIdNotInLastStation_then_exception() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = ActualUtils.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveResponse = subwayCallApi.saveSection(lineId, Param.신분당선_구간_잘못된_상행역);

        // then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 이미 등록된 역을 하행역으로 등록하면
     * Then 예외가 발생한다.
     */
    @DisplayName("하행역 등록 시 이미 등록된 역인 경우 예외")
    @Test
    void given_createSubwaySection_when_downStationIdAlreadySaveStation_then_exception() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = ActualUtils.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveResponse = subwayCallApi.saveSection(lineId, Param.신분당선_구간_잘못된_하행역);

        // then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선, 지하철 구간을 등록하고
     * When 생성한 지하철 구간을 삭제하면
     * Then 해당 지하철 구간이 삭제된다.
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSubwaySection() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = ActualUtils.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveSectionResponse = subwayCallApi.saveSection(lineId, Param.신분당선_구간);

        // then
        assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> findResponse = subwayCallApi.findSubwayLineById(lineId);
        Long stationId = ActualUtils.get(findResponse, "stations[2].id", Long.class);
        ExtractableResponse<Response> response = subwayCallApi.deleteSectionById(lineId, stationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선, 지하철 구간을 등록하고
     * When 하행 종점역이 아닌 다른 역을 삭제하면
     * Then 예외가 발생한다.
     */
    @DisplayName("지하철 노선의 하행 종점역이 아닌 다른 역 삭제 시 예외")
    @Test
    void given_createSubwaySection_when_deleteNotLastStationId_then_exception() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = ActualUtils.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveSectionResponse = subwayCallApi.saveSection(lineId, Param.신분당선_구간);

        // then
        assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> findResponse = subwayCallApi.findSubwayLineById(lineId);
        Long stationId = ActualUtils.get(findResponse, "stations[0].id", Long.class);
        ExtractableResponse<Response> response = subwayCallApi.deleteSectionById(lineId, stationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 구간이 1개인 상태에서 삭제하면
     * Then 예외가 발생한다.
     */
    @DisplayName("지하철 구간이 1개인 경우 삭제 시 예외")
    @Test
    void given_createSubwaySection_when_deleteOneOfSection_then_exception() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = ActualUtils.get(saveLineResponse, "id", Long.class);
        Long stationId = ActualUtils.get(saveLineResponse, "stations[1].id", Long.class);
        ExtractableResponse<Response> response = subwayCallApi.deleteSectionById(lineId, stationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * 파라미터 관련 클래스
     */
    private static class Param {
        public static final Map<String, Object> 신분당선 = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );

        public static final Map<String, Object> 신분당선_구간 = Map.of(
                "upStationId", 2,
                "downStationId", 3,
                "distance", 10
        );

        public static final Map<String, Object> 신분당선_구간_잘못된_상행역 = Map.of(
                "upStationId", 5,
                "downStationId", 2,
                "distance", 10
        );

        public static final Map<String, Object> 신분당선_구간_잘못된_하행역 = Map.of(
                "upStationId", 2,
                "downStationId", 1,
                "distance", 10
        );

        public static final Map<String, String> 강남역 = Map.of("name", "강남역");

        public static final Map<String, String> 양재역 = Map.of("name", "양재역");

        public static final Map<String, String> 양재시민의숲역 = Map.of("name", "양재시민의숲역");
    }
}
