package nextstep.subway.acceptance.line;

import java.util.HashMap;
import java.util.Map;

class LineParams {
    static Map<String, String> GTXA노선_연신내_서울역;
    static Map<String, String> GTXA노선_상행_정보없음;
    static Map<String, String> GTXA노선_하행_정보없음;
    static Map<String, String> GTXA노선_거리_음수;
    static Map<String, String> 신분당선;
    static Map<String, String> 노선색상;

    static Map<String, String> GTXA노선_구간_서울역_삼성역;
    static Map<String, String> GTXA노선_구간_연신내역_삼성역;
    static Map<String, String> GTXA노선_구간_서울역_연신내역;

    static Map<String, String> GTXA노선_구간_삭제_삼성역;
    static Map<String, String> GTXA노선_구간_삭제_연신내;
    static Map<String, String> GTXA노선_구간_삭제_서울역;

    public static void 파람_초기화() {
        GTXA노선_연신내_서울역 = new HashMap<>();
        GTXA노선_연신내_서울역.put("name", "GTX-A");
        GTXA노선_연신내_서울역.put("color", "bg-red-900");
        GTXA노선_연신내_서울역.put("upStationId", "1");
        GTXA노선_연신내_서울역.put("downStationId", "2");
        GTXA노선_연신내_서울역.put("distance", "10");

        GTXA노선_상행_정보없음 = new HashMap<>();
        GTXA노선_상행_정보없음.put("name", "GTX-A");
        GTXA노선_상행_정보없음.put("color", "bg-red-900");
        GTXA노선_상행_정보없음.put("downStationId", "2");
        GTXA노선_상행_정보없음.put("distance", "10");

        GTXA노선_하행_정보없음 = new HashMap<>();
        GTXA노선_하행_정보없음.put("name", "GTX-A");
        GTXA노선_하행_정보없음.put("color", "bg-red-900");
        GTXA노선_하행_정보없음.put("upStationId", "1");
        GTXA노선_하행_정보없음.put("distance", "10");

        GTXA노선_거리_음수 = new HashMap<>();
        GTXA노선_거리_음수.put("name", "GTX-A");
        GTXA노선_거리_음수.put("color", "bg-red-900");
        GTXA노선_거리_음수.put("upStationId", "1");
        GTXA노선_거리_음수.put("downStationId", "2");
        GTXA노선_거리_음수.put("distance", "-1");

        신분당선 = new HashMap<>();
        신분당선.put("name", "신분당선");
        신분당선.put("color", "bg-red-500");
        신분당선.put("upStationId", "2");
        신분당선.put("downStationId", "3");
        신분당선.put("distance", "10");

        노선색상 = new HashMap<>();
        노선색상.put("color", "bg-red-800");

        GTXA노선_구간_서울역_삼성역 = new HashMap<>();
        GTXA노선_구간_서울역_삼성역.put("upStationId", "2");
        GTXA노선_구간_서울역_삼성역.put("downStationId", "3");
        GTXA노선_구간_서울역_삼성역.put("distance", "10");

        GTXA노선_구간_연신내역_삼성역 = new HashMap<>();
        GTXA노선_구간_연신내역_삼성역.put("upStationId", "1");
        GTXA노선_구간_연신내역_삼성역.put("downStationId", "3");
        GTXA노선_구간_연신내역_삼성역.put("distance", "10");

        GTXA노선_구간_서울역_연신내역 = new HashMap<>();
        GTXA노선_구간_서울역_연신내역.put("upStationId", "2");
        GTXA노선_구간_서울역_연신내역.put("downStationId", "1");
        GTXA노선_구간_서울역_연신내역.put("distance", "10");

        GTXA노선_구간_삭제_삼성역 = new HashMap<>();
        GTXA노선_구간_삭제_삼성역.put("stationId", "3");

        GTXA노선_구간_삭제_연신내 = new HashMap<>();
        GTXA노선_구간_삭제_연신내.put("stationId", "1");

        GTXA노선_구간_삭제_서울역 = new HashMap<>();
        GTXA노선_구간_삭제_서울역.put("stationId", "2");
    }
}
