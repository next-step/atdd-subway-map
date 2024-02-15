package support.fixture;

import java.util.Map;

public class StationFixture {


    public static final String 서울역 = "서울역";
    public static final String 청량리역 = "청량리역";
    public static final String 강남역 = "강남역";
    public static final String 교대역 = "교대역";
    public static final String NAME = "name";

    public static Map<String, String> 서울역_생성() {
        return 지허철역_생성(서울역);
    }

    public static Map<String, String> 청량리역_생성() {
        return 지허철역_생성(청량리역);
    }

    public static Map<String, String> 강남역_생성() {
        return 지허철역_생성(강남역);
    }

    public static Map<String, String> 교대역_생성() {
        return 지허철역_생성(교대역);
    }


    public static Map<String, String> 지허철역_생성(String name) {
        return Map.of(NAME, name);
    }


}
