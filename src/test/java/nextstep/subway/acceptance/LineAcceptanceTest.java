package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.utils.RestAssuredCRUD;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 1개 생성 요청한다.
     * Then 지하철 노선 1개 생성된다.
     *
     * When 지하철 노선 목록 조회한다.
     * Then 지하철 노선 목록 1개 확인한다.
     *
     * When 지하철 노선 1개 있는 것 조회한다.
     * Then 지하철 노선 1개 확인한다.
     *
     * When 지하철 노선 1개 있는 것 수정한다.
     * Then 지하철 노선 1개 수정된다.
     *
     * When 지하철 노선 1개 있는 것 조회한다.
     * Then 지하철 노선 1개 수정된 것 확인한다.
     *
     * When 지하철 노선 1개 있는 것 삭제한다.
     * Then 지하철 노선 1개 삭제된다.
     *
     * When 지하철 노선 목록 조회한다.
     * Then 지하철 노선 목록이 없다.
     */
    @Test
    void lineAcceptanceTest() {
        // given
        LineRequest 라인요청테스트 = LineRequest.of("1호선", "bg-red-600");
        LineRequest 라인수정요청테스트 = LineRequest.of("2호선", "bg-blue-600");

        // when
        ExtractableResponse<Response> 노선등록결과 = 지하철_노선을_등록한다(라인요청테스트);
        // then
        assertThat(노선등록결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(노선등록결과.header("Location")).isNotBlank();

        // when
        ExtractableResponse<Response> 노선목록조회결과 = 지하철_노선_목록을_조회한다();
        // then
        assertThat(노선목록조회결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = 노선목록조회결과.jsonPath().getList("name");
        assertThat(lineNames).contains(라인요청테스트.getName());

        // when
        ExtractableResponse<Response> 노선조회결과 = 지하철_노선을_조회한다(노선등록결과.as(LineResponse.class).getId());
        // then
        assertThat(노선조회결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(노선조회결과.as(LineResponse.class).getName()).isEqualTo(라인요청테스트.getName());

        // when
        ExtractableResponse<Response> 노선수정결과 = 지하철_노선을_수정한다(
            노선등록결과.as(LineResponse.class).getId(), 라인수정요청테스트);
        // then
        assertThat(노선수정결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(노선수정결과.as(LineResponse.class).getName()).isEqualTo(라인수정요청테스트.getName());

        // when
        노선조회결과 = 지하철_노선을_조회한다(노선수정결과.as(LineResponse.class).getId());
        // then
        assertThat(노선조회결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(노선조회결과.as(LineResponse.class).getName()).isEqualTo(라인수정요청테스트.getName());

        // when
        ExtractableResponse<Response> 노선삭제결과 = 지하철_노선을_삭제한다(노선수정결과.as(LineResponse.class).getId());
        // then
        assertThat(노선삭제결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // when
        노선목록조회결과 = 지하철_노선_목록을_조회한다();
        // then
        assertThat(노선목록조회결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        lineNames = 노선목록조회결과.jsonPath().getList("name");
        assertThat(lineNames).isEmpty();
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선을_등록한다(
            LineRequest.of("1호선", "bg-red-600")
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
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
        지하철_노선을_등록한다(LineRequest.of("1호선", "bg-red-600"));
        지하철_노선을_등록한다(LineRequest.of("7호선", "bg-green-600"));

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록을_조회한다();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains("1호선", "7호선");
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
        LineResponse 노선등록결과 = 지하철_노선을_등록한다(
            LineRequest.of("2호선", "bg-green-600")
        ).as(LineResponse.class);

        // when
        ExtractableResponse<Response> 노선조회결과 = 지하철_노선을_조회한다(노선등록결과.getId());

        // then
        assertThat(노선조회결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(노선조회결과.as(LineResponse.class).getName()).isEqualTo("2호선");
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
        LineResponse 노선등록결과 = 지하철_노선을_등록한다(
            LineRequest.of("5호선", "bg-black-600")
        ).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선을_수정한다(노선등록결과.getId(),
            LineRequest.of("5.5호선", "bg-black-100"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class).getName()).isEqualTo("5.5호선");
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
        LineResponse 노선등록결과 = 지하철_노선을_등록한다(
            LineRequest.of("1호선", "bg-black-600")
        ).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선을_삭제한다(노선등록결과.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 지하철_노선을_등록한다(LineRequest request) {
        return RestAssuredCRUD.postRequest("/lines", request);
    }

    private ExtractableResponse<Response> 지하철_노선_목록을_조회한다() {
        return RestAssuredCRUD.get("/lines");
    }

    private ExtractableResponse<Response> 지하철_노선을_조회한다(Long lineId) {
        return RestAssuredCRUD.get("/lines/"+lineId);
    }

    private ExtractableResponse<Response> 지하철_노선을_수정한다(Long lineId, LineRequest lineRequest) {
        return RestAssuredCRUD.putRequest("/lines/"+lineId, lineRequest);
    }

    private ExtractableResponse<Response> 지하철_노선을_삭제한다(Long lineId) {
        return RestAssuredCRUD.delete("/lines/"+lineId);
    }
}
