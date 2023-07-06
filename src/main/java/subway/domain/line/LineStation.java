package subway.domain.line;

import subway.domain.station.Station;

import javax.persistence.*;

@Entity
public class LineStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @ManyToOne
    private Station station;

    protected LineStation() {
    }

    public LineStation(Line line, Station station) {
        this.line = line;
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
