package nextstep.subway.acceptance.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Station;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.common.CommonStationAcceptance.getParamsStationMap;
import static nextstep.subway.acceptance.common.CommonStationAcceptance.지하철역_생성_요청;

public class CommonLineAcceptance {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String lineName, String lineColor, String distance, String upStationId, String downStationId) {
            Map<String, String> params =
                    getParamsLineMap(lineName, lineColor, upStationId, downStationId, distance);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 노선_역목록_조회(String location) {
        return RestAssured
                .given().log().all()
                .when().get(location)
                .then().log().all().extract();

    }

    public static Map<String, String> getParamsLineMap(String name, String color, String upStationId, String downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    public static Map<String, String> getParamsLineMap(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }
}
