package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineRequestBuilder;
import nextstep.subway.steps.LineSteps;
import nextstep.subway.steps.StationSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private LineRequestBuilder defaultLineRequestBuilder;
    private final String UP_STATION_NAME = "강남역";
    private final String DOWN_STATION_NAME = "판교역";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> upStationResponse = StationSteps.executeStationCreateRequest(UP_STATION_NAME);
        Long upStationId = upStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> downStationResponse = StationSteps.executeStationCreateRequest(DOWN_STATION_NAME);
        Long downStationId = downStationResponse.jsonPath().getLong("id");

        defaultLineRequestBuilder = LineRequestBuilder.ofDefault()
                .withUpStationId(upStationId)
                .withDownStationId(downStationId);
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        LineRequest lineRequest = defaultLineRequestBuilder.build();
        ExtractableResponse<Response> response = LineSteps.executeLineCreateRequest(lineRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(lineRequest.getName());
        assertThat(response.jsonPath().getString("color")).isEqualTo(lineRequest.getColor());
        assertThat(response.jsonPath().getString("stations")).contains(UP_STATION_NAME, DOWN_STATION_NAME);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(lineRequest.getDistance());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복 이름 지하철 노선 생성")
    @Test
    void createDuplicatedLine() {
        LineRequest lineRequest = defaultLineRequestBuilder.build();
        LineSteps.executeLineCreateRequest(lineRequest);

        ExtractableResponse<Response> response = LineSteps.executeLineCreateRequest(lineRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        LineRequest defaultLineRequest = defaultLineRequestBuilder.build();
        LineSteps.executeLineCreateRequest(defaultLineRequest);

        String lineName = "2호선";
        String lineColor = "bg-green-600";
        LineRequest lineRequest = defaultLineRequestBuilder
                .withName(lineName)
                .withColor(lineColor)
                .build();
        LineSteps.executeLineCreateRequest(lineRequest);

        ExtractableResponse<Response> response = LineSteps.executeLineListGetRequest();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(LineRequestBuilder.DEFAULT_LINE_NAME, lineName);

        List<String> lineColors = response.jsonPath().getList("color");
        assertThat(lineColors).contains(LineRequestBuilder.DEFAULT_LINE_COLOR, lineColor);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        LineRequest lineRequest = defaultLineRequestBuilder.build();
        ExtractableResponse<Response> createdResponse = LineSteps.executeLineCreateRequest(lineRequest);

        Long createdId = createdResponse.jsonPath().getLong("id");
        ExtractableResponse<Response> response = LineSteps.executeLineGetRequest(createdId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(LineRequestBuilder.DEFAULT_LINE_NAME);
        assertThat(response.jsonPath().getString("color")).isEqualTo(LineRequestBuilder.DEFAULT_LINE_COLOR);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        LineRequest lineRequest = defaultLineRequestBuilder.build();
        ExtractableResponse<Response> createdResponse = LineSteps.executeLineCreateRequest(lineRequest);
        Long createdId = createdResponse.jsonPath().getLong("id");

        String updateName = "구분당선";
        String updateColor = "bg-blue-600";
        ExtractableResponse<Response> response = LineSteps.executeLineUpdateRequest(createdId, updateName, updateColor);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        LineRequest lineRequest = defaultLineRequestBuilder.build();
        ExtractableResponse<Response> createdResponse = LineSteps.executeLineCreateRequest(lineRequest);

        Long createdId = createdResponse.jsonPath().getLong("id");
        ExtractableResponse<Response> response = LineSteps.executeLineDeleteRequest(createdId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
