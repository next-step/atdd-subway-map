package nextstep.subway.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.HttpRequestResponse.post;

public class StationFixture {

    public static final String 강남역_이름 = "강남역";
    public static final String 역삼역_이름 = "역삼역";

    public static ExtractableResponse<Response> 역_생성(String name) {
        Map<String, String> station = station(name);

        // when
        return post(station, "/stations");
    }

    private static Map<String, String> station(String name) {
        Map<String, String> station = new HashMap<>();
        station.put("name", name);
        return station;
    }
}
