package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.global.error.exception.ErrorCode.ALREADY_REGISTERED_STAION;
import static subway.global.error.exception.ErrorCode.STATION_NOT_FINAL;
import static subway.response.LineAcceptanceTestUtils.*;
import static subway.response.SectionAcceptanceTestUtils.*;
import static subway.response.StationAcceptanceTestUtils.createStation;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private Long station1Id;
    private Long station2Id;
    private Long line1Id;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        station1Id = createStation("강남역").jsonPath().getLong("id");
        station2Id = createStation("양재역").jsonPath().getLong("id");
        Map<String, Object> line1 = createLine("신분당선", "bg-red-600", station1Id, station2Id, 10);
        line1Id = createLineResponse(line1).jsonPath().getLong("id");
        createSectionResponse(line1Id, createSectionCreateParams(station1Id, station2Id));
    }

    /**
     * given 새로운 역을 생성하고
     * when 지하철 구간을 등록하면
     * Then 지하철 구간을 조회 시 등록한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addSection() {
        //given
        Long station3Id = createStation("청계산입구역").jsonPath().getLong("id");

        //when
        createSectionResponse(line1Id, createSectionCreateParams(station2Id, station3Id));

        //then
        ExtractableResponse<Response> response = getLineResponse(line1Id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(station1Id, station2Id, station3Id);

    }

    /**
     * given 새로운 역을 생성하고
     * when 해당 노선에 등록되어 있지 않은 지하철 구간을 등록하면
     * Then STATION_NOT_FINAL 예외발생
     */
    @DisplayName("지하철 구간 등록 예외처리1 - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void addSection_exception1() {
        //given
        Long station3Id = createStation("청계산입구역").jsonPath().getLong("id");
        Long station4Id = createStation("판교역").jsonPath().getLong("id");
        //when
        ExtractableResponse<Response> sectionResponse = createSectionResponse(line1Id, createSectionCreateParams(station3Id, station4Id));
        //then
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(sectionResponse.jsonPath().getList("errorMessages")).contains(STATION_NOT_FINAL.getMessage());
    }

    /**
     * when 새로운 구간의 하행역이 해당 노선에 등록되어있는 역이면은
     * Then STATION_NOT_FINAL 예외발생
     */
    @DisplayName("지하철 구간 등록 예외처리2 - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void addSection_exception2() {
        //when
        ExtractableResponse<Response> sectionResponse = createSectionResponse(line1Id, createSectionCreateParams(station2Id, station1Id));
        //then
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(sectionResponse.jsonPath().getList("errorMessages")).contains(ALREADY_REGISTERED_STAION.getMessage());
    }

    /**
     * given 새로운 역을 생성하고 지하철 구간을 등록하고
     * when 지하철 구간을 삭제하면
     * Then 지하철 구간을 조회 시 제거된 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void deleteSection() {
        //given
        Long station3Id = createStation("청계산입구역").jsonPath().getLong("id");
        createSectionResponse(line1Id, createSectionCreateParams(station2Id, station3Id));

        //when
        ExtractableResponse<Response> deleteSectionResponse = deleteSectionResponse(line1Id, station3Id);

        //then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> response = getLineResponse(line1Id);
        assertThat(response.jsonPath().getList("stations.id", Long.class)).doesNotContain(station3Id);

    }
}
