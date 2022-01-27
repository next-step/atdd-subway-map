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
    private static final String FIRST_NAME = "신분당선";
    private static final String FIRST_COLOR = "bg-red-700";
    private static final String RESPONSE_HEADER_LOCATION = "Location";
    private static final int DEFAULT_DISTANCE = 5;
    private static final String FIRST_STATION_NAME = "강남역";
    private static final String SECOND_STATION_NAME = "양재역";
    private static final String THIRD_STATION_NAME = "양재시민의숲";
    private static final String FOURTH_STATION_NAME = "판교역";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = LineSteps.create(FIRST_NAME, FIRST_COLOR);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(RESPONSE_HEADER_LOCATION)).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        LineSteps.create(FIRST_NAME, FIRST_COLOR);

        // when
        ExtractableResponse<Response> response = LineSteps.get();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(FIRST_NAME);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        String name = "2호선";
        String color = "bg-green-600";

        LineSteps.create(FIRST_NAME, FIRST_COLOR);
        LineSteps.create(name, color);

        // when
        ExtractableResponse<Response> response = LineSteps.get();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(FIRST_NAME, name);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createLine = LineSteps.create(FIRST_NAME, FIRST_COLOR);

        // when
        ExtractableResponse<Response> response = LineSteps.modify(createLine.header(RESPONSE_HEADER_LOCATION), "구분당선", "bg-red-600");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createLine = LineSteps.create(FIRST_NAME, FIRST_COLOR);

        // when
        ExtractableResponse<Response> response = LineSteps.delete(createLine.header(RESPONSE_HEADER_LOCATION));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 이름 중복 확인")
    @Test
    void duplicateLineName() {
        // given
        LineSteps.create(FIRST_NAME, FIRST_COLOR);

        // when
        ExtractableResponse<Response> response = LineSteps.create(FIRST_NAME, FIRST_COLOR);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * When 지하철 노선 구간 생성을 요청 하면
     * Then 지하철 노선 구간이 생성된다
     */
    @DisplayName("지하철 노선 구간 등록")
    @Test
    void saveSection() {
        // given
        ExtractableResponse<Response> Line = LineSteps.create(FIRST_NAME, FIRST_COLOR);
        ExtractableResponse<Response> station1 = StationSteps.create(FIRST_STATION_NAME);
        ExtractableResponse<Response> station2 = StationSteps.create(SECOND_STATION_NAME);

        // when
        ExtractableResponse<Response> response
                = SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station1), getStationId(station2), DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 노선 구간 생성을 요청 하고
     * Given 지하철 노선 구간 생성을 요청 하고
     * Given 지하철 노선 구간 생성을 요청 하고
     * When 지하철 노선 조회를 요청 하면
     * Then 지하철 노선이 구간까지여 조회된다.
     */
    @DisplayName("지하철 노선 구간 조회")
    @Test
    void getLineAndSection() {
        // given
        ExtractableResponse<Response> line = LineSteps.create(FIRST_NAME, FIRST_COLOR);
        ExtractableResponse<Response> station1 = StationSteps.create(FIRST_STATION_NAME);
        ExtractableResponse<Response> station2 = StationSteps.create(SECOND_STATION_NAME);
        ExtractableResponse<Response> station3 = StationSteps.create(THIRD_STATION_NAME);
        ExtractableResponse<Response> station4 = StationSteps.create(FOURTH_STATION_NAME);

        SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), getStationId(station1), getStationId(station2), DEFAULT_DISTANCE);
        SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), getStationId(station2), getStationId(station3), DEFAULT_DISTANCE);
        SectionSteps.create(line.header(RESPONSE_HEADER_LOCATION), getStationId(station3), getStationId(station4), DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = LineSteps.get(getLineId(line));

        // then
        List<String> names = response.jsonPath().getList("stations.name");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(names).contains(FIRST_STATION_NAME, SECOND_STATION_NAME, THIRD_STATION_NAME, FOURTH_STATION_NAME);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * When 이미 등록된 역으로 하행역으로 구간 등록을 요청하면
     * Then 지하철 노선에 구간 등록이 실패한다.
     */
    @DisplayName("이미 구간 등록된 상행역을 상행역 구간 등록")
    @Test
    void upStationIsRegisteredAsDownStation() {
        // given
        ExtractableResponse<Response> Line = LineSteps.create(FIRST_NAME, FIRST_COLOR);
        ExtractableResponse<Response> station1 = StationSteps.create(FIRST_STATION_NAME);
        ExtractableResponse<Response> station2 = StationSteps.create(SECOND_STATION_NAME);
        ExtractableResponse<Response> station3 = StationSteps.create(THIRD_STATION_NAME);

        SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station1), getStationId(station2), DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station1), getStationId(station3), DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 생성된 지하철 노선에 구간 등록을 요청하고
     * Given 생성된 지하철 노선에 구간 등록을 요청하고
     * When 해당 노선의 마지막 역으로 구간 삭제를 요청하면
     * Then 지하철 노선 구간이 삭제된다.
     */
    @DisplayName("노선 구간 삭제")
    @Test
    void deleteSectionStation() {
        // given
        ExtractableResponse<Response> Line = LineSteps.create(FIRST_NAME, FIRST_COLOR);
        ExtractableResponse<Response> station1 = StationSteps.create(FIRST_STATION_NAME);
        ExtractableResponse<Response> station2 = StationSteps.create(SECOND_STATION_NAME);
        ExtractableResponse<Response> station3 = StationSteps.create(THIRD_STATION_NAME);

        SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station1), getStationId(station2), DEFAULT_DISTANCE);
        SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station2), getStationId(station3), DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.delete(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station3));

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
    @DisplayName("노선 구간 중간 삭제")
    @Test
    void deleteSectionCenterStation() {
        // given
        ExtractableResponse<Response> Line = LineSteps.create(FIRST_NAME, FIRST_COLOR);
        ExtractableResponse<Response> station1 = StationSteps.create(FIRST_STATION_NAME);
        ExtractableResponse<Response> station2 = StationSteps.create(SECOND_STATION_NAME);
        ExtractableResponse<Response> station3 = StationSteps.create(THIRD_STATION_NAME);

        SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station1), getStationId(station2), DEFAULT_DISTANCE);
        SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station2), getStationId(station3), DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.delete(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station2));

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
    @DisplayName("노선 구간 1개일 때 삭제")
    @Test
    void deleteOneSectionStation() {
        // given
        ExtractableResponse<Response> Line = LineSteps.create(FIRST_NAME, FIRST_COLOR);
        ExtractableResponse<Response> station1 = StationSteps.create(FIRST_STATION_NAME);
        ExtractableResponse<Response> station2 = StationSteps.create(SECOND_STATION_NAME);

        SectionSteps.create(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station1), getStationId(station2), DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = SectionSteps.delete(Line.header(RESPONSE_HEADER_LOCATION), getStationId(station2));

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
