package subway.line.application;

import subway.line.presentation.CreateLineRequest;

public class LineDto {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    private LineDto(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static LineDto from(CreateLineRequest createLineRequest) {
        return new LineDto(
                createLineRequest.getName(),
                createLineRequest.getColor(),
                createLineRequest.getUpStationId(),
                createLineRequest.getDownStationId(),
                createLineRequest.getDistance()
        );
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
}
