package subway.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class SectionResponseHelper {
    private ExtractableResponse<Response> response;

    public SectionResponseHelper(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public Integer getStatusCode() {
        return response.statusCode();
    }
}
