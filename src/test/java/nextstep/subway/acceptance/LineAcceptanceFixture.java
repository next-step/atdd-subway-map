package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceFixture {

    public static final String RED_LINE_NAME = "redLine";
    public static final String RED_LINE_COLOR = "red";
    public static final String BLUE_LINE_NAME = "blueLine";
    public static final String BLUE_LINE_COLOR = "blue";

    public static Map<String, String> fixtureRed() {
        Map<String, String> redLine = new HashMap<>();
        redLine.put("name", RED_LINE_NAME);
        redLine.put("color", RED_LINE_COLOR);
        return redLine;
    }
    public static Map<String, String> fixtureBlue() {
        Map<String, String> blueLine = new HashMap<>();
        blueLine.put("name", BLUE_LINE_NAME);
        blueLine.put("color", BLUE_LINE_COLOR);
        return blueLine;
    }
}
