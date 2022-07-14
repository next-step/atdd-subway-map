package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StationSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    @ManyToOne(fetch = FetchType.LAZY)
    private StationLine stationLine;

    public StationSection() {
    }

    public StationSection(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void setStationLine(StationLine stationLine) {
        this.stationLine = stationLine;
    }

    public Long getId() {
        return id;
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


    public boolean isSameUpStationAndDownStation(StationSection stationSection) {
        return this.upStationId.equals(stationSection.downStationId) ;
    }
}
