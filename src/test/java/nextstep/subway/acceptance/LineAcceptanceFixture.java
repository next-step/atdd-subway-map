package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceFixture {

    public static final String RED_LINE_NAME = "redLine";
    public static final String RED_LINE_COLOR = "red";
    public static final String BLUE_LINE_NAME = "blueLine";
    public static final String BLUE_LINE_COLOR = "blue";
    public static final Map<String, String> FIXTURE_RED = new HashMap<>();
    public static final Map<String, String> FIXTURE_BLUE = new HashMap<>();

    static {
        FIXTURE_RED.put("name", RED_LINE_NAME);
        FIXTURE_RED.put("color", RED_LINE_COLOR);
        FIXTURE_RED.put("upStationId", "1");
        FIXTURE_RED.put("downStationId", "1");
        FIXTURE_RED.put("distance", "10");


        FIXTURE_BLUE.put("name", BLUE_LINE_NAME);
        FIXTURE_BLUE.put("color", BLUE_LINE_COLOR);
        FIXTURE_BLUE.put("upStationId", "1");
        FIXTURE_BLUE.put("downStationId", "1");
        FIXTURE_BLUE.put("distance", "10");
    }
}
