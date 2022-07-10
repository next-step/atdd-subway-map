package nextstep.subway.applicaion.dto.stationLine;

import nextstep.subway.domain.StationLine;

public class CreateStationLineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public StationLine toEntity(){
        return new StationLine(name, color, upStationId, downStationId, distance);
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
