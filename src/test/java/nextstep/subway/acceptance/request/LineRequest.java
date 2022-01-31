package nextstep.subway.acceptance.request;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineRequest {
    public static final String PATH_PREFIX = "/lines";
    public static final String NAME = "name";
    public static final String COLOR = "color";
    public static final String LOCATION = "Location";
    public static final String UP_STATION_ID = "upStationId";
    public static final String DOWN_STATION_ID = "downStationId";
    public static final String DISTANCE = "distance";

    /** 반복되는 생성 코드를 줄이기 위해 createRequest 를 따로 작성 */
    public static ExtractableResponse<Response> lineCreateRequest(
            String name, String color, Long upStationId, Long downStationId, int distance) {

        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put(NAME, name);
        createRequest.put(COLOR, color);
        createRequest.put(UP_STATION_ID, upStationId);
        createRequest.put(UP_STATION_ID, downStationId);
        createRequest.put(DISTANCE, distance);
        return RestAssured.given()
                .log()
                .all()
                .body(createRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(PATH_PREFIX)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> specificLineReadRequest(String url) {
        return RestAssured.given().log().all().when().get(url).then().log().all().extract();
    }

    public static ExtractableResponse<Response> lineUpdateRequest(
            String uri, String name, String color) {

        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put(NAME, name);
        updateRequest.put(COLOR, color);

        return RestAssured.given()
                .log()
                .all()
                .body(updateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then()
                .log()
                .all()
                .extract();
    }
}
