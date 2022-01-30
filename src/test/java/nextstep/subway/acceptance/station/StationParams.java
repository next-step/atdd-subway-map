package nextstep.subway.acceptance.station;

import java.util.HashMap;
import java.util.Map;

public class StationParams {
    public static Map<String, String> 연신내역;
    public static Map<String, String> 서울역;
    public static Map<String, String> 삼성역;
    public static Map<String, String> 강남역;
    public static Map<String, String> 양재역;
    public static Map<String, String> 역삼역;

    public static void 파람_초기화() {
        연신내역 = new HashMap<>();
        연신내역.put("name", "연신내");
        서울역 = new HashMap<>();
        서울역.put("name", "서울역");
        삼성역 = new HashMap<>();
        삼성역.put("name", "삼성역");
        강남역 = new HashMap<>();
        강남역.put("name", "강남역");
        양재역 = new HashMap<>();
        양재역.put("name", "양재역");
        역삼역 = new HashMap<>();
        역삼역.put("name", "역삼역");
    }
}
