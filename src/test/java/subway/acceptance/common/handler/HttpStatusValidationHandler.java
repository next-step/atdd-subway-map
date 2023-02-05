package subway.acceptance.common.handler;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Http 상태 유효성 검사 관련
 */
public class HttpStatusValidationHandler {

    public static void HTTP_상태_CREATED_검증(ExtractableResponse<Response> 응답) {
        assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void HTTP_상태_OK_검증(ExtractableResponse<Response> 응답) {
        assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void HTTP_상태_NO_CONTENT_검증(ExtractableResponse<Response> 응답) {
        assertThat(응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void HTTP_상태_BAD_REQUEST_검증(ExtractableResponse<Response> 응답) {
        assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
