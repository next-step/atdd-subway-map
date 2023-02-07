package subway.common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class TestHelper {
    public static void 응답_코드가_일치한다(int actualStatusCode, HttpStatus expectedStatusCode) {
        assertThat(actualStatusCode).isEqualTo(expectedStatusCode.value());
    }

    public static String 생성_헤더(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

}
