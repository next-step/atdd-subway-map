package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public Section() {}

    public Section(Station upStation, Station downStation, int distance)
    {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void assignLine(Line line) {
        this.line = line;
    }

    public Station getUpStation() { return upStation;}

    public Station getDownStation() { return downStation; }

    public int getDistance() { return distance; }

    public Line getLine() {return line;}

    public boolean isEqual(Long id) {
        return this.id.equals(id);
    }

    public Long getId() {
        return this.id;
    }
}
