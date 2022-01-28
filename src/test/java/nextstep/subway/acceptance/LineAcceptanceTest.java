package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        LineRequest params = LineSteps.신분당선_요청_생성();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getLong("id")).isEqualTo(1);
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성(LineSteps.신분당선_요청_생성());
        ExtractableResponse<Response> createResponse2 = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/lines")
            .then().log().all()
            .extract();

        //then
        List<String> stationNames = response.jsonPath().getList("name");
        List<String> colors = response.jsonPath().getList("color");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationNames).contains("신분당선", "2호선");
        assertThat(colors).contains("bg-red-600", "bg-green-600");
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.신분당선_요청_생성()).body().as(LineResponse.class).getId();
        Long stationId1 = StationSteps.지하철역_생성("미금역").body().as(StationResponse.class).getId();
        Long stationId2 = StationSteps.지하철역_생성("정자역").body().as(StationResponse.class).getId();
        SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_조회(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("id")).isEqualTo(1);
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(response.jsonPath().getString("stations[0].name")).isEqualTo("미금역");
        assertThat(response.jsonPath().getString("stations[1].name")).isEqualTo("정자역");


    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        long lineId = LineSteps.지하철_노선_생성(LineSteps.신분당선_요청_생성())
            .as(LineResponse.class).getId();

        LineRequest editParams = LineRequest.of(
            "구분당선",
            "bg-blue-600"
        );

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_수정(lineId, editParams);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        long lineId = LineSteps.지하철_노선_생성(LineSteps.신분당선_요청_생성())
            .as(LineResponse.class).getId();

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_삭제(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 노선 생성 중복 체크")
    @Test
    void duplicateLine() {
        // given
        LineSteps.지하철_노선_생성(LineSteps.신분당선_요청_생성());

        // when
        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성(LineSteps.신분당선_요청_생성());

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
