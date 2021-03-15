package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.constants.TestConstants;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseTestUtils {
    public static void 응답_헤더_로케이션_값_있음(ExtractableResponse<Response> response) {
        assertThat(response.header(TestConstants.HTTP_HEADER_LOCATION)).isNotBlank();
    }

    public static void 응답_상태코드_확인(ExtractableResponse<Response> responseOfGetAllLines, HttpStatus httpStatus) {
        assertThat(responseOfGetAllLines.statusCode()).isEqualTo(httpStatus.value());
    }

}
