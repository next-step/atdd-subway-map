package subway.section;

import subway.exception.SectionAlreadyCreateStationException;
import subway.exception.SectionUpStationNotMatchException;
import subway.line.Line;
import subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    @ManyToOne
    @JoinColumn
    private Line line;

    private Long distance;

    public Section() {
    }

    public Section(Station upStation, Station downStation, long distance, Line line) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    public long getDistance() {
        return distance;
    }

    public void changeLine(Line line) {
        this.line = line;
    }

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }
}
