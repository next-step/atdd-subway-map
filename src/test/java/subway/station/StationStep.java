package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.util.Extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationStep {

    private static final String STATION_URL = "/stations";
    public static final String 강남역 = "강남역";
    public static final String 역삼역 = "역삼역";
    public static final String 신논현역 = "신논현역";
    public static final String 판교역 = "판교역";

    public static ExtractableResponse<Response> 지하철역을_저장(String name) {
        return Extractor.post(STATION_URL, getStationParams(name));
    }

    public static List<String> 전체_지하철역의_이름을_조회() {

        return 전체_지하철역을_조회()
            .jsonPath().getList("name", String.class);
    }

    public static ExtractableResponse<Response> 전체_지하철역을_조회() {

        return Extractor.get(STATION_URL);
    }

    public static ExtractableResponse<Response> 지하철역을_삭제(Long id) {
        return Extractor.delete(STATION_URL + "/" + id);
    }

    private static Map<String, String> getStationParams(String name) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return params;
    }
}
