package subway.section;

import subway.line.Line;
import subway.station.Station;

import javax.persistence.Column;
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

    @Column(nullable = false)
    private Long distance;

    public Section(Station station, Line line, Long distance) {
        this.station = station;
        this.line = line;
        this.distance = distance;
    }

    public void delete() {
        this.station = null;
        this.line = null;
    }

    public Long getDistance() {
        return distance;
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
