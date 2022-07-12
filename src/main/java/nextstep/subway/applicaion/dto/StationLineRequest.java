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

    public void updateExistedStationLine(StationLine stationLine){
        if(this.name != null) stationLine.setName(this.name);
        if(this.color != null) stationLine.setColor(this.color);
        if(this.upStationId != null) stationLine.setUpStationId(this.upStationId);
        if(this.downStationId != null) stationLine.setDownStationId(this.downStationId);
        if(this.distance != null) stationLine.setDistance(this.distance);
    }
}
