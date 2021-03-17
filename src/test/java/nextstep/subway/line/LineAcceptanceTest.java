package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationSteps.지하철_역_생성;
import static nextstep.subway.utils.HttpAssertions.*;
import static nextstep.subway.utils.HttpTestUtils.리소스_ID;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));

        LineRequest lineRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성(lineRequest);

        // then
        응답_HTTP_CREATED(response);
        지하철_노선_포함됨(response);
        지하철_역_포함됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));

        LineRequest lineRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        지하철_노선_생성(lineRequest);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성(lineRequest);

        // then
        응답_HTTP_BAD_REQUEST(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));

        LineRequest lineRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        지하철_노선_생성(lineRequest);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        응답_HTTP_OK(response);
        지하철_노선_목록_포함됨(response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));

        LineRequest lineRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        Long 신분당선_ID = 리소스_ID(지하철_노선_생성(lineRequest));

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선_ID);

        // then
        응답_HTTP_OK(response);
        지하철_노선_포함됨(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));

        LineRequest createRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        지하철_노선_생성(createRequest);

        // when
        LineRequest updateRequest = new LineRequest().name("2호선").color("green");
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(updateRequest);

        // then
        응답_HTTP_OK(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));

        LineRequest lineRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        ExtractableResponse<Response> createResponse = 지하철_노선_생성(lineRequest);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(리소스_ID(createResponse));

        // then
        응답_HTTP_NO_CONTENT(response);
    }

    private void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response) {
        List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class);
        Assertions.assertThat(lineResponses).isNotEmpty();
    }

    private void 지하철_노선_포함됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        Assertions.assertThat(lineResponse).isNotNull();
    }

    private void 지하철_역_포함됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        Assertions.assertThat(lineResponse.getStations()).isNotEmpty();
    }

}
