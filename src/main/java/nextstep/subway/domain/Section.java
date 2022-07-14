package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long distance;
    private Long upStationId;
    private Long downStationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stationLine")
    private StationLine stationLine;

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Section() {
    }

    public Section(Long distance, Long upStationId, Long downStationId, StationLine stationLine) {
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.stationLine = stationLine;
    }
}
