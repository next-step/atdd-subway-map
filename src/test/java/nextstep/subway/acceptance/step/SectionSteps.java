package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class SectionSteps {
    static final String 노선 = "lineId";
    static final String 상행종점 = "upStationId";
    static final String 하행종점 = "downStationId";
    static final String 거리 = "distance";

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId, Long upStationId, Long downStationId, int distance) {
        final Map<String, Object> line = createSectionRequest(lineId, upStationId, downStationId, distance);

        return RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{id}/sections", lineId)
                .then()
                .log().all().extract();
    }

    public static Map<String, Object> createSectionRequest(Long lineId, Long upStationId, Long downStationId, int distance) {
        return Map.of(
                노선, lineId,
                상행종점, upStationId,
                하행종점, downStationId,
                거리, distance
        );
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {

        return RestAssured
                .given().log().all()
                .param("stationId", stationId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{id}/sections", lineId)
                .then()
                .log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_조회_요청(Long lineId) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/{id}/sections", lineId)
                .then()
                .log().all().extract();
    }
}
