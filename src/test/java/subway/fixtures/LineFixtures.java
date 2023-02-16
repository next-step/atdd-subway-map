package subway.fixtures;

import java.util.HashMap;
import java.util.Map;

public class LineFixtures {

    public static Map<String, String> 신분당선_파라미터_생성(String upStationId, String downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");
        return params;
    }

    public static Map<String, String> 분당선_파라미터_생성(String upStationId, String downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "분당선");
        params.put("color", "bg-green-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");
        return params;
    }

    public static Map<String, String> 신분당선_수정_파라미터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선_수정");
        params.put("color", "bg-yellow-600");
        return params;
    }

    public static Map<String, String> 구간_등록_파라미터_생성(String upStationId, String downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");
        return params;
    }

}
