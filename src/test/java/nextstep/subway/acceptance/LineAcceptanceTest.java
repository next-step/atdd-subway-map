package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.utils.RestAssuredCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private LineRequest 일호선1;
    private LineRequest 이호선2;
    private LineRequest 칠호선7;

    @BeforeEach
    public void setUp() {
        super.setUp();

        일호선1 = LineRequest.of("1호선", "bg-red-600");
        이호선2 = LineRequest.of("2호선", "bg-green-600");
        칠호선7 = LineRequest.of("7호선", "bg-darkgreen-600");
    }

    @DisplayName("지하철 노선 관련 CRUD 인수테스트")
    @Test
    void lineAcceptanceTest() {
        // when
        ExtractableResponse<Response> 노선등록결과 = 지하철_노선을_등록한다(일호선1);
        // then
        지하철_노선_생성된다(노선등록결과);

        // when
        ExtractableResponse<Response> 노선목록조회결과 = 지하철_노선_목록을_조회한다();
        // then
        지하철_노선목록조회_결과에_원하는_라인이_있다(노선목록조회결과, Arrays.asList(일호선1));

        // when
        ExtractableResponse<Response> 노선조회결과 = 지하철_노선을_조회한다(노선등록결과.as(LineResponse.class).getId());
        // then
        지하철_노선조회_결과에_원하는_정보가_있다(노선조회결과, 일호선1);

        // when
        ExtractableResponse<Response> 노선수정결과 = 지하철_노선을_수정한다(노선등록결과.as(LineResponse.class).getId(), 이호선2);
        // then
        지하철_노선수정_결과에_원하는_정보가_있다(노선수정결과, 이호선2);

        // when
        노선조회결과 = 지하철_노선을_조회한다(노선수정결과.as(LineResponse.class).getId());
        // then
        지하철_노선조회_결과에_원하는_정보가_있다(노선조회결과, 이호선2);

        // when
        ExtractableResponse<Response> 노선삭제결과 = 지하철_노선을_삭제한다(노선수정결과.as(LineResponse.class).getId());
        // then
        지하철_노선삭제_되었다(노선삭제결과);

        // when
        노선목록조회결과 = 지하철_노선_목록을_조회한다();
        // then
        지하철_노선삭제_결과가_비어있다(노선목록조회결과);
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선을_등록한다(일호선1);

        // then
        지하철_노선_생성된다(response);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        지하철_노선을_등록한다(일호선1);
        지하철_노선을_등록한다(칠호선7);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록을_조회한다();

        // then
        지하철_노선목록조회_결과에_원하는_라인이_있다(response, Arrays.asList(일호선1, 칠호선7));
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        LineResponse 노선등록결과 = 지하철_노선을_등록한다(이호선2).as(LineResponse.class);

        // when
        ExtractableResponse<Response> 노선조회결과 = 지하철_노선을_조회한다(노선등록결과.getId());

        // then
        지하철_노선조회_결과에_원하는_정보가_있다(노선조회결과, 이호선2);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        LineResponse 노선등록결과 = 지하철_노선을_등록한다(일호선1).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선을_수정한다(노선등록결과.getId(), 이호선2);

        // then
        지하철_노선수정_결과에_원하는_정보가_있다(response, 이호선2);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        LineResponse 노선등록결과 = 지하철_노선을_등록한다(일호선1).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선을_삭제한다(노선등록결과.getId());

        // then
        지하철_노선삭제_되었다(response);
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

    private void 지하철_노선_생성된다(ExtractableResponse<Response> 노선등록결과) {
        assertThat(노선등록결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(노선등록결과.header("Location")).isNotBlank();
    }

    private void 지하철_노선목록조회_결과에_원하는_라인이_있다(ExtractableResponse<Response> 노선목록조회결과, List<LineRequest> 라인요청테스트) {
        assertThat(노선목록조회결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = 노선목록조회결과.jsonPath().getList("name");
        List<String> names = 라인요청테스트.stream()
            .map(LineRequest::getName)
            .collect(Collectors.toList());
        assertThat(lineNames).containsAll(names);
    }

    private void 지하철_노선조회_결과에_원하는_정보가_있다(ExtractableResponse<Response> 노선조회결과, LineRequest 라인요청테스트) {
        assertThat(노선조회결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(노선조회결과.as(LineResponse.class).getName()).isEqualTo(라인요청테스트.getName());
    }

    private void 지하철_노선수정_결과에_원하는_정보가_있다(ExtractableResponse<Response> 노선수정결과, LineRequest 라인수정요청테스트) {
        assertThat(노선수정결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(노선수정결과.as(LineResponse.class).getName()).isEqualTo(라인수정요청테스트.getName());
    }

    private void 지하철_노선삭제_되었다(ExtractableResponse<Response> 노선삭제결과) {
        assertThat(노선삭제결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 지하철_노선삭제_결과가_비어있다(ExtractableResponse<Response> 노선삭제결과) {
        assertThat(노선삭제결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> lineIds = 노선삭제결과.jsonPath().getList(".", LineResponse.class).stream()
            .map(LineResponse::getId)
            .collect(Collectors.toList());
        assertThat(lineIds).isEmpty();
    }
}
