package subway.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class SectionResponseHelper extends ResponseHelper {
    public SectionResponseHelper(ExtractableResponse<Response> response) {
        super(response);
    }
}
