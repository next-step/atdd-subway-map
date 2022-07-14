package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;
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

    public Section toSection(StationLine stationLine){
        return new Section(this.distance, this.upStationId, this.downStationId, stationLine);
    }

    public StationLine toEntity(){
        return new StationLine(this.name, this.color);
    }
}
