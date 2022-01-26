package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.RestAssuredRequest.post;

public class StationSteps {
    private static final String STATIONS_PATH = "/stations";

    public static final Map<String, String> 강남역 = new HashMap<>();
    public static final Map<String, String> 역삼역 = new HashMap<>();
    public static final Map<String, String> 양재역 = new HashMap<>();
    public static final Map<String, String> 판교역 = new HashMap<>();
    static {
        강남역.put("name", "강남역");
        역삼역.put("name", "역삼역");
        양재역.put("name", "양재역");
        판교역.put("name", "판교역");
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> params) {
        return post(params, STATIONS_PATH);
    }
}
