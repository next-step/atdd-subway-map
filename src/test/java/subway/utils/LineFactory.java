package subway.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LineFactory {

    // {key: 구간 아이디, { key: 지하철역 아이디, value: 지하철역 이름 }}
    private final Map<String, HashMap<String, String>> STATION_INFO = new HashMap<> ()
    {{
            put("1", new HashMap<>() {{
                put("1", "신사역");
                put("2", "강남역");
                put("3", "양재역");
                put("4", "광교역");
            }});
            put("2", new HashMap<>() {{
                put("5", "신설동역");
                put("6", "보문역");
                put("7", "솔샘역");
                put("8", "북한산우이역");
            }});
            put("3", new HashMap<>() {{
                put("9", "김포공항역");
                put("10", "강남역");
                put("11", "양재역");
                put("12", "광교역");
            }});
        }};

    // { key: 구간 아이디, value: 구간 이름 }
    private final HashMap<String, String> LINE_INFO = new HashMap<>() {{
        put("1", "신분당선");
        put("2", "우이신설선");
        put("3", "공항철도선");
    }};


    public static List<Map<String, String>> create(int numberOfLine) {
        List<Map<String, String>> lineList = new ArrayList<>();
        for (int i = 0; i < numberOfLine; i++) {
            lineList.add(new HashMap<>() {{

            }});
        }

        return lineList;
    }
}
