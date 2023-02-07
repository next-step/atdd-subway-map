package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.AcceptanceTest;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.line.LineAcceptanceTest.getLineParams;
import static subway.line.LineAcceptanceTest.지하철_노선_생성_요청;
import static subway.line.LineAcceptanceTest.지하철_노선_조회_요청;
import static subway.station.StationAcceptanceTest.지하철역_생성_요청;

@DisplayName("지하철 노선에 구간 등록 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private static final String ID = "id";

    private Long 강남역_id;
    private Long 판교역_id;
    private Long 정자역_id;
    private Long 신분당선_id;
    private Map<String, String> createLineParams;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역_id = 지하철역_생성_요청("강남역").jsonPath().getLong(ID);
        판교역_id = 지하철역_생성_요청("판교역").jsonPath().getLong(ID);
        정자역_id = 지하철역_생성_요청("정자역").jsonPath().getLong(ID);

        createLineParams = getLineParams("신분당선", "red", 강남역_id, 판교역_id, 14);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 노선에 새로운 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시 등록된 구간의 역들을 조회할 수 있다.
     */
    @DisplayName("지하철 노선에 구간(역)을 등록한다.")
    @Test
    void addLineSection() {
        // given
        신분당선_id = 지하철_노선_생성_요청(createLineParams).jsonPath().getLong(ID);

        // when
        지하철_노선에_지하철_구간_등록_요청(신분당선_id, 판교역_id, 정자역_id, 3);

        // then
        List<String> lineStationNames = 지하철_노선_조회_요청(신분당선_id).jsonPath().getList("stations.name");
        Set<String> stationNames = new LinkedHashSet<>(lineStationNames);

        assertThat(stationNames).containsExactly("강남역", "판교역", "정자역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 노선에 새로운 지하철 구간을 등록하는데, 새로운 지하철 구간의 상행역이 노선에 등록된 하행 종점역이 아니면
     * Then 지하철 노선에 새로운 지하철 구간을 등록할 수 없다.
     */
    @DisplayName("지하철 노선에 구간(역)을 등록하는데, 새로운 구간의 상행역이 지하철 노선의 하행 종점역이 아니면 노선에 구간을 등록할 수 없다.")
    @Test
    void addLineSectionException1() {
        // given
        신분당선_id = 지하철_노선_생성_요청(createLineParams).jsonPath().getLong(ID);


        // when
        Long 미금역_id = 지하철역_생성_요청("미금역").jsonPath().getLong(ID);
        ExtractableResponse<Response> addLineSectionResponse = 지하철_노선에_지하철_구간_등록_요청(신분당선_id, 정자역_id, 미금역_id, 3);

        // then
        assertThat(addLineSectionResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 노선에 새로운 지하철 구간을 등록하는데, 새로운 지하철 구간의 하행역이 이미 노선에 등록된 역이면
     * Then 지하철 노선에 새로운 지하철 구간을 등록할 수 없다.
     */
    @DisplayName("지하철 노선에 구간(역)을 등록하는데, 새로운 구간의 하행역이 이미 노선에 등록된 역이면 노선에 구간을 등록할 수 없다.")
    @Test
    void addLineSectionException2() {
        // given
        신분당선_id = 지하철_노선_생성_요청(createLineParams).jsonPath().getLong(ID);

        // when
        ExtractableResponse<Response> addLineSectionResponse = 지하철_노선에_지하철_구간_등록_요청(신분당선_id, 판교역_id, 강남역_id, 14);

        // then
        assertThat(addLineSectionResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성하고 새로운 구간을 등록한 뒤에
     * When 지하철 노선의 하행 종점역을 제거하면 (마지막 구간 제거)
     * Then 지하철 노선 조회 시 삭제한 마지막 구간은 조회할 수 없다.
     */
    @DisplayName("지하철 노선에 등록된 구간(역)을 제거한다.")
    @Test
    void removeLineSection() {
        // given
        신분당선_id = 지하철_노선_생성_요청(createLineParams).jsonPath().getLong(ID);
        지하철_노선에_지하철_구간_등록_요청(신분당선_id, 판교역_id, 정자역_id, 3);

        // when
        지하철_노선에_지하철_구간_제거_요청(신분당선_id, 정자역_id);

        // then
        List<String> lineStationNames = 지하철_노선_조회_요청(신분당선_id).jsonPath().getList("stations.name");
        Set<String> stationNames = new LinkedHashSet<>(lineStationNames);

        assertThat(stationNames).containsExactly("강남역", "판교역");
    }

    /**
     * Given 지하철 노선을 생성하고 새로운 구간을 등록한 뒤에
     * When 지하철 노선의 하행 종점역이 아닌 역을 제거하면
     * Then 지하철 노선에서 삭제하려는 역에 해당 구간을 제거할 수 없다.
     */
    @DisplayName("지하철 노선에 등록된 하행종점역이 아닌 역을 제거할 수 없다.")
    @Test
    void removeLineSectionException1() {
        // given
        신분당선_id = 지하철_노선_생성_요청(createLineParams).jsonPath().getLong(ID);
        지하철_노선에_지하철_구간_등록_요청(신분당선_id, 판교역_id, 정자역_id, 3);

        // when & then
                assertThatThrownBy(() -> 지하철_노선에_지하철_구간_제거_요청(신분당선_id, 판교역_id))
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * Given 지하철 노선을 생성하고 (구간 1개)
     * When 지하철 노선의 하행 종점역을 제거하면
     * Then 지하철 노선에서 삭제하려는 역에 해당 구간을 제거할 수 없다.
     */
    @DisplayName("지하철 노선에 등록된 구간이 1개이면 그 구간은 제거할 수 없다.")
    @Test
    void removeLineSectionException2() {
        // given
        신분당선_id = 지하철_노선_생성_요청(createLineParams).jsonPath().getLong(ID);

        // when & then
        assertThatThrownBy(() -> 지하철_노선에_지하철_구간_제거_요청(신분당선_id, 판교역_id))
                .isInstanceOf(RuntimeException.class);
    }

    public static Map<String, String> getSectionParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> addSectionParams = new HashMap<>();
        addSectionParams.put("upStationId", String.valueOf(upStationId));
        addSectionParams.put("downStationId", String.valueOf(downStationId));
        addSectionParams.put("distance", String.valueOf(distance));
        return addSectionParams;
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철_구간_등록_요청(Long lineId, Long upStationId, Long downStationId, int distance) {
        Map<String, String> addSectionParams = getSectionParams(upStationId, downStationId, distance);
        return RestAssured.given().log().all()
                .body(addSectionParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .extract();
    }
}
