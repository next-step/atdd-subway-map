package subway.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StationLineSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long stationLineId;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private int distance;

    public StationLineSection() {
    }

    public StationLineSection(Long stationLineId, Long upStationId, Long downStationId,
        int distance) {
        this.stationLineId = stationLineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public List<Long> getStationIdList() {
        return List.of(upStationId, downStationId);
    }

    public boolean isEqualsDownStation(long downStationId) {
        return this.downStationId.equals(downStationId);
    }
}
