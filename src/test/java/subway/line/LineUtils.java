package subway.line;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineUtils {

    public static ExtractableResponse<Response> 지하철노선_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = createParams(name, color, upStationId, downStationId, distance);

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철노선_목록조회() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_조회(Long id) {
        return RestAssured.given().log().all()
            .when().get("/lines/" + id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_수정(Long id, String name, String color) {
        Map<String, Object> params = updateParams(name, color);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/" + id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_삭제(String location) {
        return RestAssured.given().log().all()
            .when().delete(location)
            .then().log().all()
            .extract();
    }

    public static List<String> responseToStationNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("stations.name", String.class);
    }


    private static Map<String, Object> createParams(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    private static Map<String, Object> updateParams(String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

}
