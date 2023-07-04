package fixture;

import java.util.HashMap;
import java.util.Map;

public class StationModifyRequestFixture {

    public static Map<String, Object> 노선수정요청_데이터_생성(String name, String color) {

        Map<String, Object> params = new HashMap<>();

        params.put("name", name);
        params.put("color", color);

        return params;
    }}
