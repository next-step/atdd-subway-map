package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.common.DatabaseCleaner;

import java.util.Map;

@Sql("/sql/truncate-tables.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTestHelper extends AcceptanceExecutor {

    public static final String STATION_PATH = "/stations";
    public static final String LINE_PATH = "/lines";
    public static final String SECTION_PATH = "/sections";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
        databaseCleaner.execute();
    }

    public String 경로_추출(final ExtractableResponse<Response> createResponse) {
        return createResponse.header("Location");
    }

    public int 상태코드_추출(final ExtractableResponse<Response> createResponse) {
        return createResponse.statusCode();
    }

    public String 에러메시지_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().get("message");
    }

    public long 지하철역_생성(String name) {

        return post(STATION_PATH, Map.of("name", name))
                .jsonPath()
                .getLong("id");
    }
}
