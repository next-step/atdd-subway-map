package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @Embedded
    private Distance distance;

    @Embedded
    private Stations stations;

    protected Section() {
    }

    public Section(long distance, Station upStation, Station downStation) {
        this.distance = new Distance(distance);
        this.stations = new Stations(upStation, downStation);
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<Station> getStations() {
        return stations.getStations();
    }
}
