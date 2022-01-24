package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

public class StationFixture {

    public static final String 강남역 = "강남역";
    public static final String 역삼역 = "역삼역";
    public static final Map<String, String> FIXTURE_강남역 = new HashMap<>();
    public static final Map<String, String> FIXTURE_역삼역 = new HashMap<>();

    static {
        FIXTURE_강남역.put("name", 강남역);
        FIXTURE_역삼역.put("name", 역삼역);
    }

}
