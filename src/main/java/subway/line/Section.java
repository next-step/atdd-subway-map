package subway.line;

import subway.station.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    public Section(Station station, Line line) {
        this.station = station;
        this.line = line;
    }

    protected Section() {

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
