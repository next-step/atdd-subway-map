package subway.acceptance.line.fixture;

import java.util.HashMap;
import java.util.Map;

public enum LineFixture {
    지하철_3_호선("3호선", "bg-yellow-500"),
    지하철_2_호선("2호선", "bg-green-500");

    LineFixture(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private String name;
    private String color;

    public String 이름() {
        return this.name;
    }

    public String 색상() {
        return this.color;
    }

    public Map toMap() {
        Map map = new HashMap();
        map.put("name", this.name);
        map.put("color", this.color);
        return map;
    }

    public Map toMap(Long upStationId, Long downStationId, int distance) {
        Map map = new HashMap();
        map.put("name", this.name);
        map.put("color", this.color);
        map.put("upStationId", upStationId);
        map.put("downStationId", downStationId);
        map.put("distance", distance);
        return map;
    }

}