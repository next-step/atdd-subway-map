package subway.domain;

import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import subway.exception.SectionConstraintException;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Station upStation;

    @NotNull
    @OneToOne
    private Station station;

    @OneToOne
    private Station downStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Line line;

    protected Section() {
    }

    public Section(final Station station, final Station upStation, final Line line) {
        this.station = station;
        this.line = line;
        this.upStation = upStation;
        line.updateDownStation(station);
    }

    public Section(final Station upStation, final Station station, final Station downStation, final Line line) {
        this.upStation = upStation;
        this.station = station;
        this.downStation = downStation;
        this.line = line;
    }

    public Optional<Station> getUpStation() {
        return Optional.ofNullable(upStation);
    }

    public Station getStation() {
        return station;
    }

    public Optional<Station> getDownStation() {
        return Optional.ofNullable(downStation);
    }

    public Optional<Line> getLine() {
        return Optional.ofNullable(line);
    }

    public Section updateDownStation(final Station station, final Station upStation) {
        if (!line.equalDownStation(upStation)) {
            throw new SectionConstraintException();
        }
        this.downStation = station;
        return new Section(station, upStation, this.line);
    }

    public void detachLine() {
        this.line = null;
    }
}
