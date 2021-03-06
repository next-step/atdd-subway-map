package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private enum SubwayLine {

        First("1호선", "노랑색"),
        Second("2호선", "빨강색"),
        Third("3호선", "파랑색");

        public String name;
        public String color;

        SubwayLine(String name, String color) {
            this.name = name;
            this.color = color;
        }
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given & when
        ExtractableResponse<Response> response = 지하철_노선_생성요청(SubwayLine.First.name, SubwayLine.First.color);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 노선을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철_노선_생성요청(SubwayLine.First.name, SubwayLine.First.color);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성요청(SubwayLine.First.name, SubwayLine.First.color);

        // then
        지하철_노선_생성실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_생성요청(SubwayLine.First.name, SubwayLine.First.color);
        ExtractableResponse<Response> createdResponse2 = 지하철_노선_생성요청(SubwayLine.Second.name, SubwayLine.Second.color);

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
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(SubwayLine.First.name, SubwayLine.First.color);

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
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(SubwayLine.First.name, SubwayLine.First.color);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        String name = SubwayLine.Third.name;
        String color = SubwayLine.Third.color;
        ExtractableResponse<Response> response = 지하철_노선_수정요청(lineId, name, color);

        // then
        지하철_노선_수정됨(response);
        지하철_노선_확인됨(response, name, color);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성요청(SubwayLine.First.name, SubwayLine.First.color);

        // when
        Long lineId = createdResponse.as(LineResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선_제거요청(lineId);

        // then
        지하철_노선_삭제됨(response);
    }

    private ExtractableResponse<Response> 지하철_노선_생성요청(String name, String color) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철_노선_생성실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_노선목록_조회요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
        return response;
    }

    private void 지하철_노선목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.as(LineResponse.class))
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> 지하철_노선_조회요청(Long lineId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/{id}", lineId)
                .then().log().all().extract();
        return response;
    }

    private void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_포함됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        Long resultLineId = response.as(LineResponse.class).getId();
        Long expectedLineId = createdResponse.as(LineResponse.class).getId();

        assertThat(resultLineId).isEqualTo(expectedLineId);
    }

    private ExtractableResponse<Response> 지하철_노선_수정요청(Long lineId, String name, String color) {
        LineRequest request = new LineRequest(name, color);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", lineId)
                .then().log().all().extract();

        return response;
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_확인됨(ExtractableResponse<Response> response, String name, String color) {
        String updatedName = response.as(LineResponse.class).getName();
        String updatedColor = response.as(LineResponse.class).getColor();

        assertThat(updatedName).isEqualTo(name);
        assertThat(updatedColor).isEqualTo(color);
    }

    private ExtractableResponse<Response> 지하철_노선_제거요청(Long lineId) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", lineId)
                .then().log().all().extract();

        return response;
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
