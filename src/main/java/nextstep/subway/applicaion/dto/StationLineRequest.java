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

    public StationLine toUpdateEntity(StationLine stationLine){
        this.name = this.name == null ? stationLine.getName() : this.name;
        this.color = this.color == null ? stationLine.getColor() : this.color;
        this.upStationId = this.upStationId == null ? stationLine.getUpStationId() : this.upStationId;
        this.downStationId = this.downStationId == null ? stationLine.getDownStationId() : this.downStationId;
        this.distance = this.distance == null ? stationLine.getDistance() : this.distance;
        return new StationLine(stationLine.getId(), this.name, this.color, this.distance, this.upStationId, this.downStationId);
    }
}
