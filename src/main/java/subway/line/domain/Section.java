package subway.line.domain;

import subway.line.domain.exception.SectionCreateFailException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Integer distance;

    protected Section() {
    }

    public Section(
        Line line,
        Long upStationId,
        Long downStationId,
        Integer distance,
        StationValidator stationValidator
    ) {
        if (!stationValidator.existsStations(upStationId, downStationId)) {
            throw new SectionCreateFailException(
                String.format(
                    "존재하지 않는 station 입니다. StationIds: %s, %s",
                    upStationId,
                    downStationId
                )
            );
        }
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public boolean containsLastStation(Section section) {
        return this.upStationId.equals(section.downStationId) || this.downStationId.equals(section.downStationId);
    }

    public boolean isConnectable(Section other) {
        return this.downStationId.equals(other.upStationId);
    }

    public boolean isSameDownStation(Long stationId) {
        return this.downStationId.equals(stationId);
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

    public Integer getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Section{" +
            "upStationId=" + upStationId +
            ", downStationId=" + downStationId +
            '}';
    }
}
