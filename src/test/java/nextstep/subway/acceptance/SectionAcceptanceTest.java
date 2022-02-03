package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_및_아이디추출;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {
    private static final String 신분당선 = "신분당선";
    private static final String COLOR = "bg-red-600";
    private static final String 강남역 = "강남역";
    private static final String 광교역 = "양재역";
    private static final int DISTANCE = 10;
    private static final String 판교역 = "판교역";

    private long downStationId;
    private long upStationId;
    private LineResponse line;

    @BeforeEach
    public void init() {
        LineRequest lineRequest = 지하철노선_생성_요청_파라미터(신분당선, COLOR, 강남역, 광교역, DISTANCE);
        line = 지하철노선_생성_및_객체추출(lineRequest);
        downStationId = 지하철역_생성_및_아이디추출(판교역);
        int size = line.getStations().size();
        upStationId = line.getStations().get(size - 1).getId();
    }


    /**
     * When 지하철 구간 생성을 요청 하면
     * Then 지하철 구간 생성이 성공한다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createStation() {
        // when
        SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, 10);
        ExtractableResponse<Response> createResponse = 지하철구간_생성(line.getId(), sectionRequest);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }

    /**
     * When 새로운 구간의 상행역이, 하행 종점역이 아닌 구간 생성을 요청하면
     * Then 지하철 구간 생성이 실패한다.
     */
    @DisplayName("지하철 구간 등록 실패 - 하행종점역이 아님")
    @Test
    void createStationDownStationNotLastException() {
        // when
        upStationId = line.getStations().get(0).getId();
        SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, 10);
        ExtractableResponse<Response> createResponse = 지하철구간_생성(line.getId(), sectionRequest);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 구간에 이미 등록되어있는 역이 포함된 구간 생성을 요청하면
     * Then 지하철 구간 생성이 실패한다.
     */
    @DisplayName("지하철 구간 등록 실패 - 하행역이 해당노선에 이미 등록되어있음")
    @Test
    void createStationExistStationException() {
        // when
        downStationId = 1L;
        SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, 10);
        ExtractableResponse<Response> createResponse = 지하철구간_생성(line.getId(), sectionRequest);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 구간 생성을 요청 하면
     * When 지하철 구간 제거를 요청 하면
     * Then 지하철 구간 제거가 성공한다.
     */
    @DisplayName("지하철 구간 제거")
    @Test
    void deleteSection() {
        // given
        SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, 10);
        지하철구간_생성(line.getId(), sectionRequest);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철구간_삭제(line.getId(), downStationId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 구간 생성을 요청 하면
     * When 하행 종점역이 아닌, 지하철 구간 제거를 요청 하면
     * Then 지하철 구간 제거가 성공한다.
     */
    @DisplayName("지하철 구간 제거 실패 - 하행 종점역이 아님")
    @Test
    void deleteSectionNotLastStationException() {
        // given
        SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, 10);
        지하철구간_생성(line.getId(), sectionRequest);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철구간_삭제(line.getId(), 2L);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 지하철 구간 제거를 요청 하면
     * Then 지하철 구간 제거가 실패한다.
     */
    @DisplayName("지하철 구간 제거 실패 - Section 1개 이하")
    @Test
    void deleteSectionOneLeftSectionException() {
        // when
        ExtractableResponse<Response> deleteResponse = 지하철구간_삭제(line.getId(), 2L);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
