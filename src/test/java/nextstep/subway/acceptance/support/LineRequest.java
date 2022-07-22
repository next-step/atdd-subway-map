package nextstep.subway.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineRequest {
    public static long 지하철노선_생성_요청후_식별자반환(final String lineName,
                                          final String lineColor,
                                          final long upStationId,
                                          final long downStationId,
                                          final int distance) {
        return 지하철노선_생성_요청(lineName, lineColor, upStationId, downStationId, distance).jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 지하철노선_생성_요청(final String lineName,
                                                      final String lineColor,
                                                      final long upStationId,
                                                      final long downStationId,
                                                      final int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_목록조회_요청() {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/lines")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_조회_요청(final long lineId) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/lines/" + lineId)
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_삭제_요청(final long lineId) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().delete("/lines/" + lineId)
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_수정_요청(final long lineId, final String lineName, final String lineColor) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().put("/lines/" + lineId)
                          .then().log().all()
                          .extract();
    }
}
