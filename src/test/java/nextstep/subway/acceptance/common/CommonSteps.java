package nextstep.subway.acceptance.common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    public static void 생성_성공_응답(ExtractableResponse<Response> 생성_응답) {
        assertThat(생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 삭제_성공_응답(ExtractableResponse<Response> 생성_응답) {
        assertThat(생성_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}