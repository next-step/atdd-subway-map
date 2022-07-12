package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {

    public static final String NONHYUN_STATION = "논현역";
    public static final String SHIN_BUNDANG_LINE = "신분당선";
    public static final String FIRST_LINE = "1호선";
    public static final String BUNDANG_LINE = "분당선";
    public static final String RED = "bg-red-600";
    public static final String GREEN = "bg-green-600";
    public static final String BLUE = "blue";
    public static final Long DISTANCE = 5L;

    public static ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
        return RestAssured.given().log().all()
                .body(지하철_노선_생성_파라미터(name, color, String.valueOf(upStationId), String.valueOf(downStationId), String.valueOf(distance)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 지하철_노선_생성_파라미터(String name, String color, String upStationId, String downStationId, String distance) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);
        return param;
    }

}

