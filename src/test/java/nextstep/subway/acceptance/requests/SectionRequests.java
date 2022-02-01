package nextstep.subway.acceptance.requests;

import static nextstep.subway.acceptance.type.GeneralNameType.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class SectionRequests {

    public static final String UP_STATION_ID = "upStationId";
    public static final String DOWN_STATION_ID = "downStationId";
    public static final String DISTANCE = "distance";

    public static ExtractableResponse<Response> lineCreateRequest(
            String name, String color, Long upStationId, Long downStationId, int distance) {

        return RestAssured.given()
                .log()
                .all()
                .body(
                        makeLineCreationRequestBody(
                                name, color, upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> sectionAddRequest(
            Long lindId, Long upStationId, Long downStationId, int distance) {
        return RestAssured.given()
                .log()
                .all()
                .body(makeSectionAddRequestBody(upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections", lindId)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> sectionDeleteRequest(Long lineId, Long stationId) {
        return RestAssured.given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then()
                .log()
                .all()
                .extract();
    }

    private static Map<String, Object> makeLineCreationRequestBody(
            String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put(NAME.getType(), name);
        createRequest.put(COLOR.getType(), color);
        createRequest.put(UP_STATION_ID, upStationId);
        createRequest.put(DOWN_STATION_ID, downStationId);
        createRequest.put(DISTANCE, distance);
        return createRequest;
    }

    private static Map<String, Object> makeSectionAddRequestBody(
            Long upStationId, Long downStationId, int distance) {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put(UP_STATION_ID, upStationId);
        createRequest.put(DOWN_STATION_ID, downStationId);
        createRequest.put(DISTANCE, distance);
        return createRequest;
    }
}
