package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static nextstep.subway.line.LineAssertion.*;
import static nextstep.subway.line.LineRequest.*;


@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private final static String PATH = "/lines";


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        Map<String, String> body = createLineBody("7호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(body);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성하면 실패한다.")
    @Test
    void createStationWithDuplicateName() {
        String name = "1호선";

        // given
        지하철_노선_등록되어_있음(createLineBody(name, "blue"));

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(createLineBody(name, "green"));

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> line1Response = 지하철_노선_등록되어_있음(createLineBody("1호선", "blue"));
        ExtractableResponse<Response> line2Response = 지하철_노선_등록되어_있음(createLineBody("2호선", "lightgreen"));

        // when
        ExtractableResponse<Response> linesResponse = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(linesResponse);
        지하철_노선_목록_포함됨(linesResponse,
                Arrays.asList(line1Response, line2Response));
    }


    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> expectedResponse = 지하철_노선_등록되어_있음(createLineBody("1호선", "blue"));
        지하철_노선_생성_요청(createLineBody("2호선", "lightgreen"));

        // when
        Long lineId = expectedResponse
                .as(LineResponse.class)
                .getId();
        ExtractableResponse<Response> actualResponse = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선_응답됨(actualResponse, expectedResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Long lineId = 지하철_노선_등록되어_있음(createLineBody("1호선", "blue"))
                .as(LineResponse.class)
                .getId();

        // when
        String name = "9호선";
        String color = "yellow";
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(
                lineId,
                createLineBody(name, color));

        // then
        지하철_노선_수정됨(response, name, color);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        Long lineId = 지하철_노선_생성_요청(createLineBody("1호선", "blue"))
                .as(LineResponse.class)
                .getId();

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineId);

        // then
        지하철_노선_제거됨(response);
    }
}
