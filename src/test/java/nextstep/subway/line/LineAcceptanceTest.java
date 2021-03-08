package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        Map<String, String> params = makeLineParam(new Line("광교", "red"));

        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = restAssuredForPost(params);

        // then
        // 지하철_노선_생성됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = makeLineParam(new Line("광교", "red"));
        LineResponse addedLine1 = getAddedLine(restAssuredForPost(params));
        Map<String, String> params2 = makeLineParam(new Line("광교중앙", "red"));
        LineResponse addedLine2 = getAddedLine(restAssuredForPost(params2));;

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = restAssuredForGet("/lines");

        // then
        // 지하철_노선_목록_응답됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        // 지하철_노선_목록_포함됨
        List<LineResponse> lineResponseList = response.jsonPath().getList(".", LineResponse.class);
        Assertions.assertThat(Arrays.asList(addedLine1, addedLine2)).isEqualTo(lineResponseList);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = makeLineParam(new Line("광교", "red"));
        ExtractableResponse addedLineResponse = restAssuredForPost(params);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = restAssuredForGet(addedLineResponse.header("Location"));

        // then
        // 지하철_노선_응답됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(getAddedLine(response)).isEqualTo(getAddedLine(addedLineResponse));
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = makeLineParam(new Line("광교", "red"));
        LineResponse addedLine = getAddedLine(restAssuredForPost(params));

        // when
        // 지하철_노선_수정_요청
        Map<String, String> params2 = makeLineParam(new Line(addedLine.getId(), "강남", "green"));
        ExtractableResponse<Response> response = restAssuredForPut(params2);

        // then
        // 지하철_노선_수정됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(addedLine.getId()).isEqualTo(getAddedLine(response).getId());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = makeLineParam(new Line("광교", "red"));
        ExtractableResponse addedLineResponse = restAssuredForPost(params);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = restAssuredForDelete(addedLineResponse.header("Location"));

        // then
        // 지하철_노선_삭제됨
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> restAssuredForDelete(String path) {
        return RestAssured
                .given().log().all()
                .when().delete(path)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> restAssuredForPost(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> restAssuredForGet(String path) {
        return RestAssured
                .given().log().all()
                .when().get(path)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> restAssuredForPut(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("lines")
                .then().log().all().extract();
    }

    private Map<String, String> makeLineParam(Line line) {
        Map<String, String> params = new HashMap<>();
        if(line.getId() != null) {
            params.put("id", line.getId().toString());
        }
        if(line.getName() != null) {
            params.put("name", line.getName());
        }
        if(line.getColor() != null) {
            params.put("color", line.getColor());
        }
        return params;
    }

    private LineResponse getAddedLine(ExtractableResponse<Response> responseExtractableResponse) {
        return responseExtractableResponse.jsonPath().getObject(".", LineResponse.class);
    }
}
