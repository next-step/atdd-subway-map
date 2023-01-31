package subway.line;

import subway.subway.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StationLineGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    public StationLineGroup(Station station, Line line) {
        this.station = station;
        this.line = line;
    }

    protected StationLineGroup() {

    }

    public Long getId() {
        return id;
    }

    public Station getStation() {
        return station;
    }

    public Line getLine() {
        return line;
    }
}
