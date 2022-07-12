package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.StationLine;

public class StationLineRequest {
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

    public StationLine toEntity(){
        return new StationLine(this.name, this.color, this.distance, this.upStationId, this.downStationId);
    }
}
