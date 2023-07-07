package subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;
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

    public static List<LineStationConnection> createConnectionsList(List<Station> stations, Line line) {
        return stations.stream()
                .map(station -> new LineStationConnection(line, station)).collect(Collectors.toList());
    }

    public Line getLine() {
        return line;
    }

    public Station getStation() {
        return station;
    }
}
