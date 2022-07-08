package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class LineRequest {

    private String name;

    private String color;

    private int upStationId;

    private int downStationId;

    private int distance;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getUpStationId() {
        return upStationId;
    }

    public int getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Line toDomain() {
        return new Line(
                name,
                color,
                upStationId,
                downStationId,
                distance
        );
    }
}
