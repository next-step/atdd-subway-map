package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.createLineRequest;
import static nextstep.subway.acceptance.SectionSteps.addSectionRequest;
import static nextstep.subway.acceptance.SectionSteps.deleteSectionRequest;
import static nextstep.subway.acceptance.StationSteps.createStationRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        createStationRequest("강남역");
        createStationRequest("양재역");
        createStationRequest("양재시민의숲역");
        createStationRequest("청계산입구역");
        createStationRequest("판교역");
    }


    /**
     * Scenario: 구간 추가하기
     *
     * Given 지하철 노선 생성을 요청하고
     * When 생성한 노선에 지하철 구간 생성을 요청하면
     * Then 요청한 노선에 지하철 구간이 추가된다.
     */
    @DisplayName("지하철 구간 추가하기")
    @Test
    void addSection() {
        // given
        ExtractableResponse<Response> createLineResponse = createLineRequest("신분당선", "bg-red-600", 4L, 2L, 10);

        // when
        String uri = createLineResponse.header("Location");
        ExtractableResponse<Response> response = addSectionRequest(uri, 2L, 5L, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Scenario: 노선에 등록되어 있는 역으로 구간을 생성할 수 없다.
     *
     * Given 지하철 노선 생성을 요청하고
     * When 노선에 등록되어있는 역을 하행선으로 구간 등록을 요청하면
     * Then 노선에 구간 추가가 실패한다.
     */
    @DisplayName("지하철 구간 추가 실패 - 노선에 이미 등록되어있는 역을 하행역으로 생성할 경우")
    @Test
    void addSectionWithExistStationInLine() {
        // given
        ExtractableResponse<Response> createLineResponse = createLineRequest("신분당선", "bg-red-600", 4L, 2L, 10);

        // when
        String uri = createLineResponse.header("Location");
        ExtractableResponse<Response> response = addSectionRequest(uri, 2L, 4L, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Scenario: 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     *
     * Given 지하철 노선 생성을 요청하고
     * When 새로운 구간의 상행역을 해당 노선의 하행 종점역이 아닌 다른 역으로 요청하면
     * Then 노선에 구간 추가가 실패한다.
     */
    @DisplayName("지하철 구간 추가 실패 - 새로운 구간의 상행역을 해당 노선의 하행 종점역이 아닌 다른 역으로 요청할 경우")
    @Test
    void addSectionWithDoesNotLastStation() {
        // given
        ExtractableResponse<Response> createLineResponse = createLineRequest("신분당선", "bg-red-600", 4L, 2L, 10);

        // when
        String uri = createLineResponse.header("Location");
        ExtractableResponse<Response> response = addSectionRequest(uri, 3L, 4L, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Scenario: 구간 삭제하기
     *
     * Given 지하철 노선 생성을 요청하고,
     *       노선에 구간을 추가한다.
     * When 하행 종점역으로 마지막 구간 삭제 요청을 한다.
     * Then 노선에 마지막 구간이 삭제된다.
     */
    @DisplayName("지하철 구간 삭제하기")
    @Test
    void deleteSection() {
        // given
        ExtractableResponse<Response> createLineResponse = createLineRequest("신분당선", "bg-red-600", 4L, 2L, 10);
        String uri = createLineResponse.header("Location");
        addSectionRequest(uri, 2L, 5L, 10);

        // when
        ExtractableResponse<Response> response = deleteSectionRequest(uri, 5L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    /**
     * Scenario: 노선에 구간이 1개인 경우 삭제할 수 없다.
     *
     * Given 지하철 노선 생성을 요청하고
     * When 하행 종점역으로 마지막 구간 삭제 요청을 한다.
     * Then 노선에 구간 삭제가 실패한다.
     */
    @DisplayName("지하철 구간 삭제 실패 - 노선에 구간이 1개인 경우")
    @Test
    void deleteSectionFail() {
        // given
        ExtractableResponse<Response> createLineResponse = createLineRequest("신분당선", "bg-red-600", 4L, 2L, 10);
        String uri = createLineResponse.header("Location");

        // when
        ExtractableResponse<Response> response = deleteSectionRequest(uri, 2L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Scenario: 마지막 구간이 아닌 경우 삭제할 수 없다.
     *
     * Given 지하철 노선 생성을 요청하고
     * When 하행 종점역이 아닌 역으로 마지막 구간 삭제 요청을 한다.
     * Then 노선에 구간 삭제가 실패한다.
     */
    @DisplayName("지하철 구간 삭제 실패 - 마지막 구간 하행역이 아닌 경우")
    @Test
    void deleteSectionNotLastStation() {
        // given
        ExtractableResponse<Response> createLineResponse = createLineRequest("신분당선", "bg-red-600", 4L, 2L, 10);
        String uri = createLineResponse.header("Location");
        addSectionRequest(uri, 2L, 5L, 10);

        // when
        ExtractableResponse<Response> response = deleteSectionRequest(uri, 2L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
