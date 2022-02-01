package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    LineRequest 이호선;
    LineRequest 신분당선;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 교대역;

    @BeforeEach
    void 초기생성() {
        // given
        교대역 = StationSteps.지하철역_생성_요청(StationSteps.Station.교대역.getName()).as(StationResponse.class);
        역삼역 = StationSteps.지하철역_생성_요청(StationSteps.Station.역삼역.getName()).as(StationResponse.class);
        강남역 = StationSteps.지하철역_생성_요청(StationSteps.Station.강남역.getName()).as(StationResponse.class);
        이호선 = LineSteps.노선_데이터(LineSteps.Line.이호선.getName());
        신분당선 = LineSteps.노선_데이터(LineSteps.Line.신분당선.getName());
    }

    /**`
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_생성_요청(LineRequest.of(이호선.getName(), 이호선.getColor(), 교대역.getId(), 역삼역.getId(), 10));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     *  Given 지하철 노선 생성을 요청 하고
     *  When 같은 이름으로 지하철 노선 생성을 요청 하면
     *  Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 이름 중복")
    @Test
    void duplicateLineName() {
        // given
        LineSteps.지하철_노선_생성_요청(LineRequest.of(이호선.getName(), 이호선.getColor(), 교대역.getId(), 역삼역.getId(), 10));

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_생성_요청(LineRequest.of(이호선.getName(), 이호선.getColor(), 교대역.getId(), 역삼역.getId(), 10));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
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
        LineSteps.지하철_노선_생성_요청(LineRequest.of(신분당선.getName(), 신분당선.getColor(), 교대역.getId(), 역삼역.getId(), 10));
        LineSteps.지하철_노선_생성_요청(LineRequest.of(이호선.getName(), 이호선.getColor(), 교대역.getId(), 역삼역.getId(), 10));

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_목록_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).containsExactly(신분당선.getName(), 이호선.getName());
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
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(LineRequest.of(신분당선.getName(), 신분당선.getColor(), 교대역.getId(), 역삼역.getId(), 10));
        String uri = createResponse.header("Location");

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_조회(uri);

        // then
        JsonPath createResponseJson = createResponse.jsonPath();
        JsonPath responseJson = response.jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseJson.getString("name")).isEqualTo(createResponseJson.getString("name"));
        assertThat(responseJson.getString("color")).isEqualTo(createResponseJson.getString("color"));
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
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(LineRequest.of(신분당선.getName(), 신분당선.getColor(), 교대역.getId(), 역삼역.getId(), 10));
        String uri = createResponse.header("Location");

        // when
        LineSteps.지하철_노선_수정(uri, 이호선);
        ExtractableResponse<Response> response = LineSteps.지하철_노선_조회(uri);

        // then
        JsonPath createResponseJson = createResponse.jsonPath();
        JsonPath responseJson = response.jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseJson.getString("name")).isNotEqualTo(createResponseJson.getString("name"));
        assertThat(responseJson.getString("color")).isNotEqualTo(createResponseJson.getString("color"));
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
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(LineRequest.of(신분당선.getName(), 신분당선.getColor(), 교대역.getId(), 역삼역.getId(), 10));
        String uri = createResponse.header("Location");

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_삭제(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
