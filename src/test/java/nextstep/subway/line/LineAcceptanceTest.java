package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static nextstep.subway.line.LineSteps.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    public static Map<String, String> 노선_2호선 = makeLine("2호선","bg-green-600");
    public static Map<String, String> 노선_1호선 = makeLine("1호선","bg-blue-600");

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(노선_2호선);

        // then
        지하철_노선_생성됨(response);
    }


    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicatedName() {
        // given
        지하철_노선_생성_요청(노선_2호선);

        // when
        ExtractableResponse<Response> response =  지하철_노선_생성_요청(노선_2호선);

        // then
        지하철_노선_생성_실패(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(노선_2호선);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(노선_1호선);

        // when
        ExtractableResponse<Response> getResponse = 지하철_노선_목록_조회();

        // then
        지하철_생성한_노선_목록_조회_성공(Stream.of(createResponse1, createResponse2), getResponse);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선);

        // when
        Long createId = parseIdFromResponseHeader(createResponse);
        ExtractableResponse<Response> getResponse = 노선_ID로_조회(createId);

        // then
        지하철_생성한_노선_조회_성공(createResponse, getResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선);
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        ExtractableResponse<Response> modifyResponse = 지하철_노선_수정_요청(노선_1호선, createId);

        // then
        지하철_노선_수정_성공(modifyResponse);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선);
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(createId);

        // then
        지하철_노선_제거_성공(deleteResponse);
    }

    public static Map<String, String> makeLine(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

}
