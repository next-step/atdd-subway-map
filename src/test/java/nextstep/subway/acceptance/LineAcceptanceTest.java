package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.steps.LineSteps;
import nextstep.subway.acceptance.steps.SectionSteps;
import nextstep.subway.acceptance.steps.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private static final String FIRST_LINE_NAME = "신분당선";
    private static final String FIRST_LINE_COLOR = "bg-red-700";
    private static final String SECOND_LINE_NAME = "2호선";
    private static final String SECOND_LINE_COLOR = "bg-green-700";
    private static final String FIRST_STATION_NAME = "강남역";
    private static final String SECOND_STATION_NAME = "양재역";
    private static final String THIRD_STATION_NAME = "양재시민의숲";
    private static final String FOURTH_STATION_NAME = "판교역";
    private static final int DEFAULT_DISTANCE = 5;
    private static final String RESPONSE_HEADER_LOCATION = "Location";

    /**
     * Given 지하철 역을 생성하고
     * Given 지하철 역을 생성하고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));

        // when
        ExtractableResponse<Response> response = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(RESPONSE_HEADER_LOCATION)).isNotBlank();
    }

    /**
     * Given 지하철 역을 생성하고
     * Given 지하철 역을 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("선택한 지하철 노선 조회한다")
    @Test
    void getLine() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = LineSteps.get(getLineId(line));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String lineName = response.jsonPath().get("name");
        assertThat(lineName).isEqualTo(FIRST_LINE_NAME);
    }

    /**
     * Given 지하철 역을 생성하고
     * Given 지하철 역을 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void getLines() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));

        LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);
        LineSteps.create(SECOND_LINE_NAME, SECOND_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = LineSteps.get();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(FIRST_LINE_NAME, SECOND_LINE_NAME);
    }

    /**
     * Given 지하철 역을 생성하고
     * Given 지하철 역을 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선의 정보를 수정한다.")
    @Test
    void updateLine() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = LineSteps.modify(line.header(RESPONSE_HEADER_LOCATION), "구분당선", "bg-red-600");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 역을 생성하고
     * Given 지하철 역을 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = LineSteps.delete(line.header(RESPONSE_HEADER_LOCATION));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 역을 생성하고
     * Given 지하철 역을 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성 요청이 실패한다.
     */
    @DisplayName("지하철 노선의 이름이 중복이면 생성할 수 없다.")
    @Test
    void duplicateLineName() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));

        LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 역을 생성하고
     * Given 지하철 역을 생성하고
     * Given 지하철 노선 생성을 요청 하고
     * Given 추가될 지하철 역 생성을 요청 하고
     * Given 추가될 지하철 역 생성을 요청 하고
     * When 추가된 역으로 지하철 노선 구간 생성을 요청 하면
     * Then 새로운 지하철 노선의 구간이 추가된다.
     */
    @DisplayName("지하철 노선의 새로운 구간을 등록한다.")
    @Test
    void addSection() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));
        Long lastStationId = getStationId(StationSteps.create(THIRD_STATION_NAME));

        ExtractableResponse<Response> Line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), downStationId, lastStationId, DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * When 선택한 지하철 노선 조회를 요청 하면
     * Then 선택한 지하철 노선이 구간까지 조회된다.
     */
    @DisplayName("선택한 지하철 노선을 조회한다.")
    @Test
    void getLineAndSection() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));
        Long newUpStationId = getStationId(StationSteps.create(THIRD_STATION_NAME));
        Long newDownStationId = getStationId(StationSteps.create(FOURTH_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);
        SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), downStationId, newUpStationId, DEFAULT_DISTANCE);
        SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), newUpStationId, newDownStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = LineSteps.get(getLineId(line));

        // then
        List<String> names = response.jsonPath().getList("stations.name");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(names).contains(FIRST_STATION_NAME, SECOND_STATION_NAME, THIRD_STATION_NAME, FOURTH_STATION_NAME);
    }

    /**
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 노선 생성을 요청 하고
     * When 구간에 등록된 상행역으로 다시한번 상행역 등록을 요청하면
     * Then 지하철 노선에 구간 등록이 실패한다.
     */
    @DisplayName("새로운 구간의 상행역은 이미 등록된 구간의 상행역일 수 없다.")
    @Test
    void upStationIsRegisteredAsDownStation() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));
        Long lastStationId = getStationId(StationSteps.create(THIRD_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), upStationId, lastStationId, DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    /**
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 노선 생성을 요청 하고
     * When 구간에 등록된 역으로 다시한번 하행역으로 구간 등록을 요청하면
     * Then 지하철 노선에 구간 등록이 실패한다.
     */
    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void duplicateSectionStation() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), downStationId, upStationId, DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 노선 생성을 요청 하고
     * Given 생성된 지하철 노선에 구간 등록을 요청하고
     * When 노선의 등록된 마지막 역을 삭제 요청하면
     * Then 지하철 노선의 마지막 구간이 삭제된다.
     */
    @DisplayName("노선에 등록된 역(하행 종점역)만 제거할 수 있다")
    @Test
    void deleteSectionStation() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));
        Long lastStationId = getStationId(StationSteps.create(THIRD_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);
        SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), downStationId, lastStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.delete(line.header(RESPONSE_HEADER_LOCATION), lastStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 생성된 지하철 노선에 구간 등록을 요청하고
     * Given 생성된 지하철 노선에 구간 등록을 요청하고
     * When 해당 노선의 중간역을 구간 삭제를 요청하면
     * Then 지하철 노선 구간 삭제 요청이 실패한다.
     */
    @DisplayName("노선에 등록된 역(하행 종점역)이 아니라면 삭제할 수 없다.")
    @Test
    void deleteSectionCenterStation() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));
        Long lastStationId = getStationId(StationSteps.create(THIRD_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);
        SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), downStationId, lastStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.delete(line.header(RESPONSE_HEADER_LOCATION), downStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 생성된 지하철 노선에 구간 등록을 요청하고
     * When 해당 노선의 마지막 역으로 구간 삭제를 요청하면
     * Then 지하철 노선 구간 삭제 요청이 실패한다.
     */
    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.")
    @Test
    void deleteOneSectionStation() {
        // given
        Long upStationId = getStationId(StationSteps.create(FIRST_STATION_NAME));
        Long downStationId = getStationId(StationSteps.create(SECOND_STATION_NAME));

        ExtractableResponse<Response> line = LineSteps.create(FIRST_LINE_NAME, FIRST_LINE_COLOR, upStationId, downStationId, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.delete(line.header(RESPONSE_HEADER_LOCATION), downStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Long getStationId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
    private Long getLineId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
