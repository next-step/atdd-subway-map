package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;

import java.util.Arrays;
import java.util.List;

@Entity
public class Section {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    private Long distance;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    private Station upStation;

    @ManyToOne
    private Station downStation;

    public Section() {
    }

    public Section(Station upStation, Station downStation, Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void update(Station upStation, Station downStation, Long distance) {
        this.getUpStation().update(upStation);
        this.getDownStation().update(downStation);
        this.distance = distance != null ? distance : this.distance;
    }
    public void delete() {
        this.line = null;
    }

    public Station getUpStation() { return this.upStation; }

    public Station getDownStation() {
        return this.downStation;
    }

    public Long getUpStationId() {
        return this.getUpStation().getId();
    }

    public Long getDownStationId() {
        return this.getDownStation().getId();
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
