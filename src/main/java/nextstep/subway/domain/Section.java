package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubwayLine subwayLine;

    public Section() {
    }

    public Section(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(Section section, SubwayLine subwayLine) {
        this.upStationId = section.upStationId;
        this.downStationId = section.downStationId;
        this.distance = section.distance;
        this.subwayLine = subwayLine;
    }

    public Long getUpStationId() {
        return this.upStationId;
    }

    public Long getDownStationId() {
        return this.downStationId;
    }

    public boolean isDownStationIdFor(Long isDownStationId) {
        return this.downStationId.equals(isDownStationId);
    }

    public void clearSubwayLine() {
        this.subwayLine = null;
    }

}
