package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineVerificationSteps {

    public static void 지하철_노선_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        생성된_지하철_노선_URI_경로_존재_함(response);
    }

    public static void 지하철_노선_생성_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 생성된_지하철_노선_URI_경로_존재_함(ExtractableResponse<Response> response) {
        assertThat(생성된_지하철_노선_URI_경로_확인(response)).isNotBlank();
    }

    public static String 생성된_지하철_노선_URI_경로_확인(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

    public static void 지하철_노선_목록_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_목록_조회_결과에_2개_노선_포함_확인(ExtractableResponse<Response> readLinesResponse) {
        List<LineResponse> lines = readLinesResponse.jsonPath().getList(".", LineResponse.class);
        assertThat(lines).hasSize(2);
    }

    public static void 지하철_노선_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static Long 수정할_지하철_노선_ID_가져오기(ExtractableResponse<Response> createResponse) {
        return createResponse.as(LineResponse.class).getId();
    }

    public static void 지하철_노선_수정_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_제거_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
