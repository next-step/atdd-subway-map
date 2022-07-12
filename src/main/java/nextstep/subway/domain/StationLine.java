package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationLineRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long distance;
    private Long upStationId;
    private Long downStationId;

    public StationLine() {
    }

    public StationLine(String name, String color, Long distance, Long upStationId, Long downStationId) {
        this.name = name;
        this.distance = distance;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public void updateByStationLineRequest(StationLineRequest stationLineRequest) {
        if (stationLineRequest.getName() != null) this.name = stationLineRequest.getName();
        if (stationLineRequest.getColor() != null) this.color = stationLineRequest.getColor();
        if (stationLineRequest.getUpStationId() != null) this.upStationId = stationLineRequest.getUpStationId();
        if (stationLineRequest.getDownStationId() != null) this.downStationId = stationLineRequest.getDownStationId();
        if (stationLineRequest.getDistance() != null) this.distance = stationLineRequest.getDistance();
    }
}
