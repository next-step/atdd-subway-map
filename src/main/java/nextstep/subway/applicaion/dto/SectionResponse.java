package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class SectionResponse {
    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationid;
    private Long distance;
    private List<Station> stations;

    public SectionResponse(Section section) {
        this.id = section.getId();
        this.lineId = section.getLineId();
        this.upStationId = section.getUpStationId();
        this.downStationid = section.getDownStationId();
        this.distance = section.getDistance();
        this.stations  = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationid() {
        return downStationid;
    }

    public Long getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStation(Station station){
        stations.add(station);
    }
}
