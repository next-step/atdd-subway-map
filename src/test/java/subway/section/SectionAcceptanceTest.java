package subway.section;

import static common.CommonRestAssured.post;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.exception.ErrorCode.DUPLICATED_STATION;
import static subway.exception.ErrorCode.END_STATION_MISMATCH;
import static subway.exception.ErrorCode.INSUFFICIENT_SECTION;
import static subway.exception.ErrorCode.NOT_END_STATION;
import static subway.exception.ErrorCode.NOT_FOUND_STATION;

import common.CommonRestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.AbstractAcceptanceTest;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.station.StationAcceptanceTest;
import subway.station.StationResponse;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AbstractAcceptanceTest {

    private static StationResponse 강남역;
    private static StationResponse 미금역;
    private static StationResponse 구의역;
    private static StationResponse 서울숲역;

    @BeforeEach
    void setUp() {
        강남역 = StationAcceptanceTest.createStation("강남역");
        미금역 = StationAcceptanceTest.createStation("미금역");
        구의역 = StationAcceptanceTest.createStation("구의역");
        서울숲역 = StationAcceptanceTest.createStation("서울숲역");
    }

    private LineResponse 신분당선_생성() {
        LineRequest lineRequest = new LineRequest("신분당선", "RED", 강남역.getId(), 미금역.getId(), 10L);
        return post("lines", lineRequest).jsonPath().getObject("$", LineResponse.class);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 상행역이 해당 노선의 하행 종점역과 동일하고, 하행역은 해당 노선에 등록되어있지 않은 새로운 구간을 등록하면
     * Then 해당 구간이 지하철 노선에 등록된다.
     */
    @DisplayName("지하철 노선에 구간이 등록된다.")
    @Test
    void addSectionToLine() {
        // given
        LineResponse 신분당선 = 신분당선_생성();

        // when
        SectionRequest sectionRequest = new SectionRequest(신분당선.getDownStation().getId(),
            구의역.getId(),
            10L);
        CommonRestAssured.post("/lines/1/sections", sectionRequest);

        // then
        List<String> stationNames = getStationNames(CommonRestAssured.get("/lines/1"));
        assertThat(stationNames).contains("미금역", "구의역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 상행역이 해당 노선의 하행 종점역과 동일하지 않은 구간을 등록하면
     * Then END_STATION_MISMATCH 에러가 발생한다.
     */
    @DisplayName("상행역이 해당 노선의 하행 종점역과 동일하지 않은 구간은 지하철 노선에 등록할 수 없다.")
    @Test
    void addSectionWithMismatchEndStationError() {
        // given
        신분당선_생성();

        // when
        SectionRequest sectionRequest = new SectionRequest(강남역.getId(), 구의역.getId(), 10L);
        ExtractableResponse<Response> response = post("/lines/1/sections", sectionRequest);

        // then
        assertThat(getMessage(response)).isEqualTo(END_STATION_MISMATCH.getMessage());
    }

    private String getMessage(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("message");
    }

    private List<String> getStationNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("stations.name");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 하행역이 해당 노선에 등록되어있는 역인 구간을 등록하면
     * Then DUPLICATED_STATION 에러가 발생한다.
     */
    @DisplayName("하행역이 해당 노선에 등록되어있는 역인 구간은 지하철 노선에 등록할 수 없다.")
    @Test
    void addSectionWithDuplicatedStationError() {
        // given
        LineResponse 신분당선 = 신분당선_생성();

        // when
        SectionRequest sectionRequest = new SectionRequest(신분당선.getDownStation().getId(),
            강남역.getId(), 10L);
        ExtractableResponse<Response> response = post("/lines/1/sections", sectionRequest);

        // then
        assertThat(getMessage(response)).isEqualTo(DUPLICATED_STATION.getMessage());
    }


    /**
     * Given 구간이 2개인 지하철 노선을 생성하고
     * When 마지막 구간을 제거하면
     * Then 노선의 마지막 구간이 제거된다.
     */
    @DisplayName("지하철 노선의 마지막 구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        LineResponse 신분당선 = 신분당선_생성();
        SectionRequest sectionRequest = new SectionRequest(신분당선.getDownStation().getId(),
            구의역.getId(), 10L);
        CommonRestAssured.post("/lines/1/sections", sectionRequest);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("stationId", 구의역.getId().toString());
        ExtractableResponse<Response> response = CommonRestAssured.delete("/lines/1/sections",
            params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<String> stationNames = getStationNames(CommonRestAssured.get("/lines/1"));
        assertThat(stationNames).doesNotContain("구의역");
    }

    /**
     * Given 구간이 1개인 지하철 노선을 생성하고
     * When 구간을 제거하면
     * Then INSUFFICIENT_SECTION 에러가 발생한다.
     */
    @DisplayName("구간이 1개인 지하철 노선은 구간을 제거할 수 없다.")
    @Test
    void deleteSectionWithInsufficientSectionError() {
        // given
        LineResponse 신분당선 = 신분당선_생성();

        // when
        Map<String, String> params = new HashMap<>();
        params.put("stationId", 신분당선.getDownStation().getId().toString());
        ExtractableResponse<Response> response = CommonRestAssured.delete("/lines/1/sections",
            params);

        // then
        assertThat(getMessage(response)).isEqualTo(INSUFFICIENT_SECTION.getMessage());
    }


    /**
     * Given 구간이 2개인 지하철 노선을 생성하고
     * When 지하철 노선에 등록되지 않은 역을 제거하면
     * Then NOT_FOUND_STATION 에러가 발생한다.
     */
    @DisplayName("지하철 노선에 등록되지 않은 역은 제거할 수 없다.")
    @Test
    void deleteSectionWithNotFoundStationError() {
        // given
        LineResponse 신분당선 = 신분당선_생성();
        SectionRequest sectionRequest = new SectionRequest(신분당선.getDownStation().getId(),
            구의역.getId(), 10L);
        CommonRestAssured.post("/lines/1/sections", sectionRequest);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("stationId", 서울숲역.getId().toString());
        ExtractableResponse<Response> response = CommonRestAssured.delete("/lines/1/sections",
            params);

        // then
        assertThat(getMessage(response)).isEqualTo(NOT_FOUND_STATION.getMessage());
    }

    /**
     * Given 구간이 2개인 지하철 노선을 생성하고
     * When 하행 종점역이 아닌 구간을 제거하면
     * Then NOT_END_STATION 에러가 발생한다.
     */
    @DisplayName("하행 종점역이 아닌 구간은 제거할 수 없다.")
    @Test
    void deleteSectionWithNotEndStationError() {
        // given
        LineResponse 신분당선 = 신분당선_생성();
        SectionRequest sectionRequest = new SectionRequest(신분당선.getDownStation().getId(),
            구의역.getId(), 10L);
        CommonRestAssured.post("/lines/1/sections", sectionRequest);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("stationId", 미금역.getId().toString());
        ExtractableResponse<Response> response = CommonRestAssured.delete("/lines/1/sections",
            params);

        // then
        assertThat(getMessage(response)).isEqualTo(NOT_END_STATION.getMessage());
    }
}
