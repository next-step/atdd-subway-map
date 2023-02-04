package subway.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AcceptanceTestHelper extends AcceptanceExecutor {

    public static final String STATION_PATH = "/stations";
    public static final String LINE_PATH = "/lines";
    public static final String SECTION_PATH = "/sections";

    public String 경로_추출(final ExtractableResponse<Response> createResponse) {
        return createResponse.header("Location");
    }
}
