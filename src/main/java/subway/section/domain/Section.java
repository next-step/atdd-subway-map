package subway.section.domain;

import subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long lineId;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    private Long distance;

    public Section() {
    }

    public Section(Long lineId, Station upStation, Station downStation, Long distance) {
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
