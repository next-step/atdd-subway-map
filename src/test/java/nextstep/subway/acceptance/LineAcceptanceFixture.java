package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceFixture {

    public static final String RED_LINE_NAME = "redLine";
    public static final String RED_LINE_COLOR = "red";
    public static final String BLUE_LINE_NAME = "blueLine";
    public static final String BLUE_LINE_COLOR = "blue";


    public static Map<String, String> fixtureRed() {
        return param(RED_LINE_NAME, RED_LINE_COLOR);
    }

    public static Map<String, String> fixtureBlue() {
        return param(BLUE_LINE_NAME, BLUE_LINE_COLOR);
    }

    private static Map<String, String> param(String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        return param;
    }
}
