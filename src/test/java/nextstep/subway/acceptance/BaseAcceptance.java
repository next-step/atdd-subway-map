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

    protected ExtractableResponse<Response> 지하철_역_생성(final String station) {
        return RestAssured.given().log().all()
            .body(Map.of("name", station))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    protected long 지하철_노선_생성(final String upStation, final String downStation,
        final String lineName, final String color) {
        final ExtractableResponse<Response> upStationResponse = 지하철_역_생성(upStation);
        final ExtractableResponse<Response> downStationResponse = 지하철_역_생성(downStation);

        final long upStationId = upStationResponse.jsonPath().getLong("id");
        final long downStationId = downStationResponse.jsonPath().getLong("id");

        final ExtractableResponse<Response> lineResponse = createSubwayLine(lineName,
            color, upStationId, downStationId, 10);
        return lineResponse.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> createSubwayLine(final String name, final String color,
        final long upStationId, final long downStationId, final int distance) {
        final Map<String, Object> param = Map.of(
            "name", name,
            "color", color,
            "upStationId", upStationId,
            "downStationId", downStationId,
            "distance", distance
        );

        return RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> 전체_노선_조회() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }
}
