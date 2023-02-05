package subway.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/sql/truncate-tables.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AcceptanceTestHelper extends AcceptanceExecutor {

    public static final String STATION_PATH = "/stations";
    public static final String LINE_PATH = "/lines";
    public static final String SECTION_PATH = "/sections";

    public String 경로_추출(final ExtractableResponse<Response> createResponse) {
        return createResponse.header("Location");
    }

    public int 상태코드_추출(final ExtractableResponse<Response> createResponse) {
        return createResponse.statusCode();
    }

    public String 에러메시지_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().get("message");
    }
}
