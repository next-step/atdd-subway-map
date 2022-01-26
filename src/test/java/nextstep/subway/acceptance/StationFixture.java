package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

public class StationFixture {

    public static final String 시청역 = "시청역";
    public static final String 서울역 = "서울역";
    public static final String 용산역 = "용산역";
    public static final String 강남역 = "강남역";
    public static final String 역삼역 = "역삼역";
    public static final String 선릉역 = "선릉역";
    public static final Map<String, String> FIXTURE_시청역 = new HashMap<>();
    public static final Map<String, String> FIXTURE_서울역 = new HashMap<>();
    public static final Map<String, String> FIXTURE_용산역 = new HashMap<>();
    public static final Map<String, String> FIXTURE_강남역 = new HashMap<>();
    public static final Map<String, String> FIXTURE_역삼역 = new HashMap<>();
    public static final Map<String, String> FIXTURE_선릉역 = new HashMap<>();

    static {
        FIXTURE_시청역.put("name", 시청역);
        FIXTURE_서울역.put("name", 서울역);
        FIXTURE_용산역.put("name", 용산역);
        FIXTURE_강남역.put("name", 강남역);
        FIXTURE_역삼역.put("name", 역삼역);
        FIXTURE_선릉역.put("name", 선릉역);
    }

}
