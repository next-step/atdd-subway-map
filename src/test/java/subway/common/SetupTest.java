package subway.common;
import java.util.HashMap;
import java.util.Map;

import static subway.domain.line.LineApiTest.지하철노선에_구간을_추가한다;
import static subway.domain.line.LineApiTest.지하철노선을_생성한다;


public class SetupTest {
    public static void 분당선_노선을_생성한다() {
        Map<String, Object> param = new HashMap<>();

        param.put("name", "분당선");
        param.put("color", "bg-green-600");

        지하철노선을_생성한다(param);
    }

    public static void 신분당선_노선을_생성한다() {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "신분당선");
        param.put("color", "bg-red-600");

        지하철노선을_생성한다(param);
    }

    public static void 신분당선_노선을_생성한뒤_새로운_구간을_추가한다() {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "신분당선");
        param.put("color", "bg-red-600");

        지하철노선을_생성한다(param);

        Map<String, Object> subParam = new HashMap<>();
        subParam.put("upStationId", 1);
        subParam.put("downStationId", 2);
        subParam.put("distance", 10);
        지하철노선에_구간을_추가한다(1, subParam);

        subParam.put("upStationId", 2);
        subParam.put("downStationId", 3);
        subParam.put("distance", 5);
        지하철노선에_구간을_추가한다(1, subParam);
    }
}
