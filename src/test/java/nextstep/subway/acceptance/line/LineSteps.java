package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineSteps {

    public static final String NONHYUN_STATION = "논현역";
    public static final String SHIN_NONHYUN_STATION = "신논현역";

    public static final String SHIN_BUNDANG_LINE = "신분당선";
    public static final String FIRST_LINE = "1호선";
    public static final String BUNDANG_LINE = "분당선";
    public static final String RED = "bg-red-600";
    public static final String GREEN = "bg-green-600";
    public static final String BLUE = "blue";
    public static final Long DISTANCE = 5L;

    public static ExtractableResponse<Response> 노선_생성(String 노선명, String 노선색, Long 노선_상행역_ID, Long 노선_하행역_ID, Long distance) {
        return RestAssured.given().log().all()
                .body(노선_생성_PARAM(노선명, 노선색, String.valueOf(노선_상행역_ID), String.valueOf(노선_하행역_ID), String.valueOf(distance)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static List<Long> 노선_역_목록_ID(ExtractableResponse<Response> 노선) {
        List<Long> 노선_역_목록_ID = new ArrayList<>();
        노선_역_목록_ID.add(노선_상행역_ID(노선));
        노선_역_목록_ID.add(노선_하행역_ID(노선));
        return 노선_역_목록_ID;
    }

    public static Long 노선_상행역_ID(ExtractableResponse<Response> 노선) {
        return Long.valueOf(노선.jsonPath().getString("upStation.id"));
    }

    public static Long 노선_하행역_ID(ExtractableResponse<Response> 노선) {
        return Long.valueOf(노선.jsonPath().getString("downStation.id"));
    }

    private static Map<String, String> 노선_생성_PARAM(String name, String color, String upStationId, String downStationId, String distance) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);
        return param;
    }

}

