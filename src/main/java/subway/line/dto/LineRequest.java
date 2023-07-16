package subway.line.dto;

import subway.line.domain.Line;

public class LineRequest {

    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public Line toEntity() {
        return Line.of(id, name, color, distance);
    }

    public void saveId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public Integer getDistance() {
        return distance;
    }
}
