package subway.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long upStationId;

    private Long downStationId;

    private Integer distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    protected Section() {
    }

    public Section(Line line, Long upStationId, Long downStationId, Integer distance) {
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public boolean isDownStationId(Long stationId) {
        return downStationId.equals(stationId);
    }

    public boolean isUpStationId(Long stationId) {
        return upStationId.equals(stationId);
    }

    public boolean isUpStationOrDownStation(Long upStationId, Long downStationId) {
        return isUpStationId(upStationId) || this.isDownStationId(downStationId);
    }
}
