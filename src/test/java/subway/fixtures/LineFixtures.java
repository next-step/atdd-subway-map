package subway.fixtures;

import java.util.HashMap;
import java.util.Map;

public class LineFixtures {

    public static Map<String, String> 신분당선_파라미터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");
        return params;
    }

    public static Map<String, String> 분당선_파라미터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "분당선");
        params.put("color", "bg-green-600");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");
        return params;
    }
}
