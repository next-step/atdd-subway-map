package subway.fixtures;

import java.util.HashMap;
import java.util.Map;

public class LineSectionFixtures {

    public static Map<String, String> 구간_등록_파라미터_생성(String upStationId, String downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");
        return params;
    }
}
