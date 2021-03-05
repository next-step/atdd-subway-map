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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // given - 지하철_노선_생성_요청
        LineRequest lineRequestOfShinBundang = new LineRequest("신분당선", "bg-red-600");

        // when - 지하철_노선_생성_요청
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(lineRequestOfShinBundang);

        // then - 지하철_노선_생성됨
        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createLineResponse.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLines() {
        // given - 지하철_노선_등록되어_있음
        LineRequest lineRequestOfShinBundang = new LineRequest("신분당선", "bg-red-600");
        지하철_노선_생성_요청(lineRequestOfShinBundang);
        LineRequest lineRequestOf2Line = new LineRequest("2호선", "bg-green-600");
        지하철_노선_생성_요청(lineRequestOf2Line);

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> readLinesResponse = 지하철_노선_목록_조회_요청();

        // then - 지하철_노선_목록_응답됨
        assertThat(readLinesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        // 지하철_노선_목록_포함됨
        List<LineResponse> lines = readLinesResponse.jsonPath().getList(".", LineResponse.class);
        assertThat(lines).hasSize(2);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void getLine() {
        // given - 지하철_노선_등록되어_있음
        LineRequest lineRequestOfShinBundang = new LineRequest("신분당선", "bg-red-600");
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(lineRequestOfShinBundang);

        String uri = createResponse.header("Location");

        // when - 지하철_노선_조회_요청
        ExtractableResponse<Response> readLineResponse = 지하철_노선_조회_요청(uri);

        // then - 지하철_노선_응답됨
        assertThat(readLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given - 지하철_노선_등록되어_있음
        LineRequest lineRequestOfShinBundang = new LineRequest("신분당선", "bg-red-600");
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(lineRequestOfShinBundang);


        // when - 지하철_노선_수정_요청
        Long id = createResponse.as(LineResponse.class).getId();
        LineRequest updateRequestOfGuBundang = new LineRequest("구분당선", "bg-blue-600");
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(id, updateRequestOfGuBundang);

        // then - 지하철_노선_수정됨
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음

        // when
        // 지하철_노선_제거_요청

        // then
        // 지하철_노선_삭제됨
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, LineRequest updateRequestOfShinBundang) {
        return RestAssured
                .given().log().all()
                .body(updateRequestOfShinBundang)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .extract();
    }
}
