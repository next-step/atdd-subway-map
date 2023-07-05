package subway.line.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import subway.station.domain.Station;

@Entity
public class LineStationConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @ManyToOne
    private Station station;

    protected LineStationConnection() {}

    public LineStationConnection(Line line, Station station) {
        this.line = line;
        this.station = station;
    }

    public Line getLine() {
        return line;
    }

    public Station getStation() {
        return station;
    }
}
