package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionAcceptanceTestUtil {

    public static ExtractableResponse<Response> 지하철_구간_생성(long lineId, long upStationId, long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제(long lineId, long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("stationId", stationId)
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_조회(long sectionId) {
        return RestAssured.given().log().all()
                .when().get("/sections/" + sectionId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_전체_조회() {
        return RestAssured.given().log().all()
                .when().get("/sections")
                .then().log().all()
                .extract();
    }
}
