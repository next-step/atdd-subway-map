package nextstep.subway.utils.httpresponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class Header {
    public static String uri(ExtractableResponse<Response> response) {
        return response.header("Location");
    }
}
