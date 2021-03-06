package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

  @DisplayName("지하철 노선을 생성한다.")
  @Test
  void createLine() {
    // when
    ExtractableResponse<Response> response = 지하철_노선_생성요청("신분당선", "red");
    // then
    assertThat(response.statusCode())
            .isEqualTo(HttpStatus.CREATED.value());
  }

  @DisplayName("지하철 노선 목록을 조회한다.")
  @Test
  void getLines() {
    // given
    LineResponse lineResponse1 = 지하철_노선_생성요청("2호선", "green").as(LineResponse.class);
    LineResponse lineResponse2 = 지하철_노선_생성요청("8호선", "pink").as(LineResponse.class);

    // when
    // 지하철_노선_목록_조회_요청
    ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/lines")
                    .then()
                    .log().all().extract();

    // then
    // 지하철_노선_목록_응답됨
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    // 지하철_노선_목록_포함됨
    List<LineResponse> lineRequestResponses = Arrays.asList(lineResponse1, lineResponse2);
    List<LineResponse> lineResults = response.body().jsonPath().getList(".", LineResponse.class);

    assertThat(lineRequestResponses).isEqualTo(lineResults);
  }

  @DisplayName("지하철 노선을 조회한다.")
  @Test
  void getLine() {
    // given
    // 지하철_노선_생성_요청
    LineResponse line = 지하철_노선_생성요청("1호선", "blue").as(LineResponse.class);

    // when
    // 지하철_노선_조회_요청
    ExtractableResponse<Response> response =
            RestAssured.given().log().all()
            .when()
            .get("/lines/{id}", line.getId())
            .then().log().all()
            .extract();

    // then
    // 지하철_노선_응답됨
    지하철_노선_요청에대한_응답_확인(response, line);
  }

  @DisplayName("지하철 노선을 수정한다.")
  @Test
  void updateLine() {
    // given
    // 지하철_노선_생성_요청
    LineResponse line = 지하철_노선_생성요청("6호선", "brown").as(LineResponse.class);

    // when
    // 지하철_노선_수정_요청
    ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(line)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/lines/{id}", line.getId())
            .then().log().all()
            .extract();

    // then
    // 지하철_노선_수정됨
    지하철_노선_요청에대한_응답_확인(response, line);
  }

  @DisplayName("지하철 노선을 제거한다.")
  @Test
  void deleteLine() {
    // given
    // 지하철_노선_생성_요청
    LineResponse lineResponse = 지하철_노선_생성요청("분당선", "yellow").as(LineResponse.class);

    // when
    // 지하철_노선_제거_요청
    ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/lines/{id}", lineResponse.getId())
            .then().log().all()
            .extract();

    // then
    // 지하철_노선_삭제됨
    assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  private ExtractableResponse<Response> 지하철_노선_생성요청(String stationName, String stationColor) {
    Map<String, String> params = new HashMap<>();
    params.put("name", stationName);
    params.put("color", stationColor);

    return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then()
            .log().all().extract();
  }

  private void 지하철_노선_요청에대한_응답_확인(ExtractableResponse<Response> response, LineResponse line) {
    LineResponse lineResponse = response.as(LineResponse.class);
    assertAll(
            () -> assertEquals(line.getName(), lineResponse.getName()),
            () -> assertEquals(line.getColor(), lineResponse.getColor())
    );
  }
}
