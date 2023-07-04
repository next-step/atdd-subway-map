package fixture;

import java.util.HashMap;
import java.util.Map;

public class StationLineRequestFixture {

    public static Map<String, Object> 노선등록요청_데이터_생성(String name, String color, long upStationId, long downStationId, int distance) {

        Map<String, Object> params = new HashMap<>();

        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return params;
    }
}
