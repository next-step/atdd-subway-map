package subway.factory;

import java.util.HashMap;
import java.util.Map;

public class LineFactory {

    public static final String[] LINE_NAMES = { "신분당선", "우이신설선", "공항철도선" };

    // {key: 구간 이름, value: 구간값 { key: 구간 필드, value: 구간 필드값 }}
    private static final Map<String, HashMap<String, String>> LINE_MAP = new HashMap<> ()
    {{
            put(LINE_NAMES[0], new HashMap<>() {{
                put("name", LINE_NAMES[0]);
                put("color", "bg-red-600");
                put("upStationId", "1");
                put("downStationId", "2");
                put("distance", "10");
            }});
            put(LINE_NAMES[1], new HashMap<>() {{
                put("name", LINE_NAMES[1]);
                put("color", "bg-green-200");
                put("upStationId", "2");
                put("downStationId", "3");
                put("distance", "20");
            }});
            put(LINE_NAMES[2], new HashMap<>() {{
                put("name", LINE_NAMES[2]);
                put("color", "bg-blue-200");
                put("upStationId", "4");
                put("downStationId", "5");
                put("distance", "30");
            }});
        }};

    public static Map<String, String> create(String name) {
        return LINE_MAP.get(name);
    }

    public static Map<String, String> createNameAndColorUpdateParams(String name, String color) {
        return new HashMap<>() {{
            put("name", name);
            put("color", color);
        }};
    }
}
