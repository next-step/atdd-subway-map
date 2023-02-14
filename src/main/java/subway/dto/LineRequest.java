package subway.dto;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public class LineRequest implements SectionCreateReader {

    private String name;
    private String color;
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    private LineRequest(String name, String color, Long downStationId, Long upStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public static LineRequest of(
            String name, String color, Long downStationId, Long upStationId, Long distance) {
        return new LineRequest(name, color, downStationId, upStationId, distance);
    }
}
