package nextstep.subway.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.utils.LineAcceptanceTestUtils;
import nextstep.subway.acceptance.utils.SectionAcceptanceTestUtils;
import nextstep.subway.acceptance.utils.StationAcceptanceTestUtils;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.acceptance.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends BaseTest {

    private static final Long DISTANCE_STATION2_TO_STATION3 = 3L;

    private static final String EQUAL_CURRENT_DOWN_STATION_WITH_NEW_UP_STATION
            = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역과 같아야 합니다.";

    private static final String NOT_EXIST_NEW_DOWN_STATION
            = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.";

    private static final String ABLE_TO_DELETE_ONLY_LAST_SECTION = "하행 종점역이 포함된 구간만 제거할 수 있습니다.";

    private static final String DELETE_WHEN_SECTION_TWO_OR_MORE
            = "지하철 구간의 개수가 2 이상일 때만 구간을 제거할 수 있습니다.";

    private final StationAcceptanceTestUtils stationAcceptanceTestUtils = new StationAcceptanceTestUtils();

    private final LineAcceptanceTestUtils lineAcceptanceTestUtils = new LineAcceptanceTestUtils();

    private final SectionAcceptanceTestUtils sectionAcceptanceTestUtils = new SectionAcceptanceTestUtils();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private LineRequest LINE_5;

    private StationResponse station1;

    private StationResponse station2;

    private StationResponse station3;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        station1 = objectMapper.readValue(
                stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME1).body().asString(), StationResponse.class);
        station2 = objectMapper.readValue(
                stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME2).body().asString(), StationResponse.class);
        station3 = objectMapper.readValue(
                stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME3).body().asString(), StationResponse.class);
        LINE_5 = new LineRequest(LINE_NAME_5, LINE_COLOR_5, station1.getId(), station2.getId(), LINE_DISTANCE_5);
    }

    @AfterEach
    public void initialize() {
        databaseInitializer.execute();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 상행역이 해당 노선에 등록되어있는 하행 종점역이고
     * When 하행역이 해당 노선에 등록되어있지 않은 새로운 구간을 등록한다면
     * Then 새로운 구간이 해당 노선에 등록된다.
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void createSection() throws JsonProcessingException {
        // given
        Long lineId = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).jsonPath().getLong("id");

        // when
        SectionRequest request = sectionAcceptanceTestUtils.toSectionRequest(
                station2.getId().toString(), station3.getId().toString(), DISTANCE_STATION2_TO_STATION3);

        ExtractableResponse<Response> response = sectionAcceptanceTestUtils.지하철_구간_등록(request, lineId);

        // then
        ExtractableResponse<Response> line = lineAcceptanceTestUtils.지하철_노선_조회(lineId);

        LineResponse lineResponse = objectMapper.readValue(line.body().asString(), LineResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(lineResponse.getStations()).contains(station1, station2, station3);
    }

    /**
     * Given 지하철 노선을 생성하고
     * WHEN 새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닌 구간을 등록할 경우
     * THEN Exception 을 발생시켜, 400 error code 와 실패 사유를 message 로 전달한다.
     */
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닐 경우 Exception")
    @Test
    void notEqualCurrentDownStationIdWithNewUpStationId() {
        // given
        Long lineId = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).jsonPath().getLong("id");
        SectionRequest request = sectionAcceptanceTestUtils.toSectionRequest(
                station3.getId().toString(), station2.getId().toString(), DISTANCE_STATION2_TO_STATION3);

        // when
        ExtractableResponse<Response> response = sectionAcceptanceTestUtils.지하철_구간_등록(request, lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(EQUAL_CURRENT_DOWN_STATION_WITH_NEW_UP_STATION);
    }

    /**
     * Given 지하철 노선을 생성하고
     * WHEN 새로운 구간의 하행역이 해당 노선에 존재할 경우
     * THEN Exception 을 발생시켜, 400 error code 와 실패 사유를 message 로 전달한다.
     */
    @DisplayName("새로운 구간의 하행역이 해당 노선에 존재할 경우 Exception")
    @Test
    void notExistDownStationIdOrThrow() {
        // given
        Long lineId = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).jsonPath().getLong("id");
        SectionRequest request = sectionAcceptanceTestUtils.toSectionRequest(
                station2.getId().toString(), station1.getId().toString(), DISTANCE_STATION2_TO_STATION3);

        // when
        ExtractableResponse<Response> response = sectionAcceptanceTestUtils.지하철_구간_등록(request, lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(NOT_EXIST_NEW_DOWN_STATION);
    }

    /**
     * Given 구간이 2개인 지하철 노선을 생성하고
     * When 하행역이 포함한 구간을 제거하면
     * Then 해당 노선의 하행선과 하행선을 삭제한 구간이 제거된다.
     */
    @DisplayName("지하철 구간을 제거한다.")
    @Test
    void deleteSection() throws JsonProcessingException {
        // given
        Long lineId = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).jsonPath().getLong("id");
        SectionRequest request = sectionAcceptanceTestUtils.toSectionRequest(
                station2.getId().toString(), station3.getId().toString(), DISTANCE_STATION2_TO_STATION3);
        sectionAcceptanceTestUtils.지하철_구간_등록(request, lineId);

        // when
        ExtractableResponse<Response> response = sectionAcceptanceTestUtils.지하철_구간_제거(lineId, station3.getId());

        // then
        ExtractableResponse<Response> line = lineAcceptanceTestUtils.지하철_노선_조회(lineId);

        LineResponse lineResponse = objectMapper.readValue(line.body().asString(), LineResponse.class);
        assertThat(lineResponse.getStations()).doesNotContain(station3);
        assertThat(lineResponse.getDistance()).isEqualTo(LINE_DISTANCE_5);
    }

    /**
     * Given 구간이 2개인 지하철 노선을 생성하고
     * When 하행역이 아닌 다른 지하철역을 포함한 구간을 제거하면
     * Then Exception 을 발생시켜, 400 error code 와 실패 사유를 message 로 전달한다.
     */
    @DisplayName("하행선이 아닌 다른 지하철역을 포함한 구간을 제거할 경우 Exception")
    @Test
    void deleteOnlyLastSection() {
        // given
        Long lineId = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).jsonPath().getLong("id");
        SectionRequest request = sectionAcceptanceTestUtils.toSectionRequest(
                station2.getId().toString(), station3.getId().toString(), DISTANCE_STATION2_TO_STATION3);
        sectionAcceptanceTestUtils.지하철_구간_등록(request, lineId);

        // when
        ExtractableResponse<Response> response = sectionAcceptanceTestUtils.지하철_구간_제거(lineId, station2.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(ABLE_TO_DELETE_ONLY_LAST_SECTION);
    }

    /**
     * Given 구간이 1개인 지하철 노선을 생성하고
     * When 하행역이 아닌 다른 지하철역을 포함한 구간을 제거하면
     * Then Exception 을 발생시켜, 400 error code 와 실패 사유를 message 로 전달한다.
     */
    @DisplayName("지하철 구간이 1개일 때 구간을 제거할 경우 Exception")
    @Test
    void deleteWhenSectionTwoOrMore() {
        // given
        Long lineId = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).jsonPath().getLong("id");
        SectionRequest request = sectionAcceptanceTestUtils.toSectionRequest(
                station2.getId().toString(), station3.getId().toString(), DISTANCE_STATION2_TO_STATION3);

        // when
        ExtractableResponse<Response> response = sectionAcceptanceTestUtils.지하철_구간_제거(lineId, station2.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(DELETE_WHEN_SECTION_TWO_OR_MORE);
    }
}
