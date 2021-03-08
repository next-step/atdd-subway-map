package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.line.LineSteps.Line.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given & when
        ExtractableResponse<Response> response = 지하철_노선_생성요청(일호선.name, 일호선.color);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 노선을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철_노선_생성요청(일호선.name, 일호선.color);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성요청(일호선.name, 일호선.color);

        // then
        지하철_노선_생성실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_생성요청(일호선.name, 일호선.color);
        ExtractableResponse<Response> createdResponse2 = 지하철_노선_생성요청(분당선.name, 분당선.color);

        // when
        ExtractableResponse<Response> response = 지하철_노선목록_조회요청();

        // then
        지하철_노선목록_응답됨(response);
        지하철_노선목록_포함됨(response, Arrays.asList(createdResponse1, createdResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(일호선.name, 일호선.color);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선_조회요청(lineId);

        // then
        지하철_노선_응답됨(response);
        지하철_노선_포함됨(response, createdResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(일호선.name, 일호선.color);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        String name = 에버라인.name;
        String color = 에버라인.color;
        ExtractableResponse<Response> response = 지하철_노선_수정요청(lineId, name, color);

        // then
        지하철_노선_수정됨(response);
        지하철_노선_확인됨(response, name, color);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(일호선.name, 일호선.color);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선_제거요청(lineId);

        // then
        지하철_노선_삭제됨(response);
    }
}
