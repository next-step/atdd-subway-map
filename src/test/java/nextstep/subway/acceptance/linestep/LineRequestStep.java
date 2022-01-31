package nextstep.subway.acceptance.linestep;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.testenum.TestLine;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineRequestStep {
    private final static String BODY_ELEMENT_NAME = "name";
    private final static String BODY_ELEMENT_COLOR = "color";
    private final static String BODY_ELEMENT_ID = "id";
    private final static String BODY_UP_STATION_ID = "upStationId";
    private final static String BODY_DOWN_STATION_ID = "downStationId";
    private final static String BODY_DISTANCE = "distance";

    public static ExtractableResponse<Response> 노선_생성(TestLine line, Long 역1, Long 역2, int 거리) {
        Map<String, Object> params = new HashMap<>();

        insertRequestBody(params, BODY_ELEMENT_NAME, line.getName());
        insertRequestBody(params, BODY_ELEMENT_COLOR, line.getColor());
        insertRequestBody(params, BODY_UP_STATION_ID, 역1);
        insertRequestBody(params, BODY_DOWN_STATION_ID, 역2);
        insertRequestBody(params, BODY_DISTANCE, 거리);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .post("/lines")

                .then().log().all()
                .extract();
    }

    private static void insertRequestBody(Map<String, Object> params, String key, Object value) {
        params.put(key, value);
    }

    public static ExtractableResponse<Response> 노선_목록_조회() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/lines")

                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_조회(Long id) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/lines/" + id)

                .then().log().all()
                .extract();
    }

    public static long extractId(ExtractableResponse<Response> responseByPost) {
        return responseByPost.body().jsonPath().getLong(BODY_ELEMENT_ID);
    }

    public static ExtractableResponse<Response> 노선_변경(String lineName, String lineColor, Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL_VALUE)
                .body(patchRequestBody(lineName, lineColor))

                .when()
                .patch("/lines/" + id)

                .then().log().all()
                .extract();
    }

    private static Map<String, String> patchRequestBody(String lineName, String lineColor) {
        Map<String, String> params = new HashMap<>(1);
        params.put(BODY_ELEMENT_NAME, lineName);
        params.put(BODY_ELEMENT_COLOR, lineColor);
        return params;
    }

    public static ExtractableResponse<Response> 노선_삭제(Long deletedId) {
        return RestAssured
                .given()
                .accept(MediaType.ALL_VALUE)

                .when()
                .delete("/lines/" + deletedId)

                .then().log().all()
                .extract();
    }
}
