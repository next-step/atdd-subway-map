package nextstep.subway.utils.httpresponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class Header {
    public static String uri(ExtractableResponse<Response> response) {
        return response.header("Location");
    }
}
