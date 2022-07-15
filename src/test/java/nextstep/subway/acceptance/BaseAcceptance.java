package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import nextstep.subway.acceptance.config.DataBaseCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
public class BaseAcceptance {

    @LocalServerPort
    protected int port;

    @Autowired
    protected DataBaseCleaner dataBaseCleaner;

    protected ExtractableResponse<Response> createStation(final String station) {
        return RestAssured.given().log().all()
            .body(Map.of("name", station))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }
}
