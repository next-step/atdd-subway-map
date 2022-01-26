package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

public class LineFixture {

    public static final String 일호선_이름 = "redLine";
    public static final String 일호선_색상 = "red";
    public static final String 이호선_이름 = "blueLine";
    public static final String 이호선_색상 = "blue";
    public static final Map<String, String> FIXTURE_1호선 = new HashMap<>();
    public static final Map<String, String> FIXTURE_2호선 = new HashMap<>();

    static {
        FIXTURE_1호선.put("name", 일호선_이름);
        FIXTURE_1호선.put("color", 일호선_색상);
        FIXTURE_1호선.put("upStationId", "1");
        FIXTURE_1호선.put("downStationId", "2");
        FIXTURE_1호선.put("distance", "10");


        FIXTURE_2호선.put("name", 이호선_이름);
        FIXTURE_2호선.put("color", 이호선_색상);
        FIXTURE_2호선.put("upStationId", "1");
        FIXTURE_2호선.put("downStationId", "2");
        FIXTURE_2호선.put("distance", "10");
    }
}
