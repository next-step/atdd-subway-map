package nextstep.subway.applicaion.dto.subwayLine;

import nextstep.subway.domain.subwayLine.SubwayLine;

public class CreateSubwayLineRequest {

    private final String name;

    private final String color;

    private final Long upStationId;

    private final Long downStationId;

    private final Integer distance;

    public CreateSubwayLineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}
