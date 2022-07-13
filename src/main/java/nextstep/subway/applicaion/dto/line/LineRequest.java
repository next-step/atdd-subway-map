package nextstep.subway.applicaion.dto.line;

import nextstep.subway.domain.line.Line;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

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

    public Line toEntity() {
        return new Line.Builder()
                        .name(name)
                        .color(color)
                        .upStationId(upStationId)
                        .downStationId(downStationId)
                        .distance(distance)
                        .build();
    }
}
