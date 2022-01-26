package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.CustomRestAssured;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    public static ExtractableResponse<Response> 노선_조회(final String createdLineId) {
        return CustomRestAssured.get("/lines/" + createdLineId);
    }

    public static ExtractableResponse<Response> 노선_생성(final String lineName, final String lineColor, String upStationId, String downStationId, String distance) {
        return CustomRestAssured.post("/lines/", lineCreateParams(lineName, lineColor, upStationId, downStationId, distance));
    }

    public static ExtractableResponse<Response> 노선_정보_변경(final String id, final String name, final String color) {
        return CustomRestAssured.put("/lines/" + id, lineUpdateParams(name, color));
    }

    public static ExtractableResponse<Response> 구간_추가(final String upStationId, final String downStationId, final String lineId, String distance) {
        Map<String, String> params = sectionAddParams(upStationId, downStationId, distance);

        return CustomRestAssured.post("/lines/" + lineId + "/sections", params);
    }

    public static ExtractableResponse<Response> 구간_제거(final String lineId, String stationId) {
        return CustomRestAssured.delete("/lines/" + lineId + "/sections?stationId=" + stationId);
    }

    public static ExtractableResponse<Response> 지하철역_조회() {
        return CustomRestAssured.get("/stations");
    }

    public static ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return CustomRestAssured.post("/stations", params);
    }

    private static Map<String, String> lineCreateParams(final String lineName, final String lineColor, String upStationId, String downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return params;
    }

    private static Map<String, String> lineUpdateParams(final String lineName, final String lineColor) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);

        return params;
    }

    private static Map<String, String> sectionAddParams(final String upStationId, final String downStationId, final String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("distance", distance);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        return params;
    }
}
