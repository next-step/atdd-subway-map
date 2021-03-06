package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.LineSteps.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

  @DisplayName("지하철 노선을 생성한다.")
  @Test
  void createLine() {
    // when
    ExtractableResponse<Response> response = 지하철_노선_생성요청("신분당선", "red");
    // then
    지하철_노선_응답_확인(response.statusCode(), HttpStatus.CREATED);
  }

  @DisplayName("지하철 노선 목록을 조회한다.")
  @Test
  void getLines() {
    // given
    LineResponse lineResponse1 = 지하철_노선_생성요청("2호선", "green").as(LineResponse.class);
    LineResponse lineResponse2 = 지하철_노선_생성요청("8호선", "pink").as(LineResponse.class);

    // when
    // 지하철_노선_목록_조회_요청
    ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

    // then
    // 지하철_노선_목록_응답됨
    지하철_노선_응답_확인(response.statusCode(), HttpStatus.OK);

    // 지하철_노선_목록_포함됨
    List<LineResponse> lineRequestResponses = Arrays.asList(lineResponse1, lineResponse2);
    지하철_노선_목록_응답_확인(response, lineRequestResponses);
  }

  @DisplayName("지하철 노선을 조회한다.")
  @Test
  void getLine() {
    // given
    LineResponse line = 지하철_노선_생성요청("1호선", "blue").as(LineResponse.class);

    // when
    ExtractableResponse<Response> response = 지하철_노선_조회_요청(line);

    // then
    지하철_노선_요청에대한_응답_확인(response, line);
  }

  @DisplayName("지하철 노선을 수정한다.")
  @Test
  void updateLine() {
    // given
    LineResponse line = 지하철_노선_생성요청("6호선", "brown").as(LineResponse.class);

    // when
    ExtractableResponse<Response> response = 지하철_노선_수정_요청(line);

    // then
    지하철_노선_요청에대한_응답_확인(response, line);
  }

  @DisplayName("지하철 노선을 제거한다.")
  @Test
  void deleteLine() {
    // given
    LineResponse lineResponse = 지하철_노선_생성요청("분당선", "yellow").as(LineResponse.class);

    // when
   ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse);

    // then
    지하철_노선_응답_확인(response.statusCode(), HttpStatus.NO_CONTENT);
  }
}
