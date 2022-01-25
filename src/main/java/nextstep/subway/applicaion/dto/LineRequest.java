package nextstep.subway.applicaion.dto;

import javax.validation.constraints.NotEmpty;

public class LineRequest {

    private String name;
    private String color;

    @NotEmpty
    private Long upStationId;

    @NotEmpty
    private Long downStationId;

    @NotEmpty
    private int distance;

    protected LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId,
        int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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

    public int getDistance() {
        return distance;
    }

}
