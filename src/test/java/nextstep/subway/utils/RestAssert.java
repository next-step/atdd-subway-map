package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class RestAssert {
    private ExtractableResponse<Response> response;

    private RestAssert(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public static RestAssert that(ExtractableResponse<Response> response) {
        return new RestAssert(response);
    }

    public void 응답_상태_확인(HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
