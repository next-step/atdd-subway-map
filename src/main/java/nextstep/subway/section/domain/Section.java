package nextstep.subway.section.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
public class Section extends BaseEntity implements Comparable<Section>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "up_station_id")
    @ManyToOne
    private Station upStation;

    @JoinColumn(name = "down_station_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Station downStation;
    private int distance;

    @JoinColumn(name = "line_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Line line;

    public Section() {
    }

    private Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Station upStation, Station downStation, int distance) {
        return new Section(upStation, downStation, distance);
    }

    public void addLine(Line line) {
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public boolean isEqual(Station upStation) {
        return Objects.equals(this.downStation, upStation);
    }

    public Stream<Station> getStations() {
        return Stream.of(upStation, downStation);
    }

    @Override
    public int compareTo(Section section) {
        if(downStation.equals(section.getDownStation()) ) {
            return -1;
        }
        if(upStation.equals(section.getUpStation())) {
            return 1;
        }
        return 0;
    }

    public void remove() {
        this.downStation = null;
    }
}
