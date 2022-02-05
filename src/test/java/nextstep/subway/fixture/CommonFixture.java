package nextstep.subway.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CommonFixture {
    public static String uri(ExtractableResponse<Response> response) {
        return response.header("Location");
    }
}
