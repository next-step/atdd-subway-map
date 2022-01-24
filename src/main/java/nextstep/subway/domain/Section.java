package nextstep.subway.domain;

import nextstep.subway.exception.DuplicatedStationsOfLineException;
import nextstep.subway.exception.OutOfSectionDistanceException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section extends BaseEntity {
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

    public Section() {

    }
    private Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        validateDistance(distance);
        validateStation(upStation, downStation);
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Long id, Line line, Station upStation, Station downStation, int distance) {
        return new Section(id, line, upStation, downStation, distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(null, line, upStation, downStation, distance);
    }

    public Long getId() {
        return this.id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public void validateDistance(int distance) {
        if (distance < 1) {
            throw new OutOfSectionDistanceException(distance);
        }
    }

    public void validateStation(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new DuplicatedStationsOfLineException(upStation, downStation);
        }
    }

    public boolean hasStation(Station station) {
        return getDownStation().equals(station) || getUpStation().equals(station);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(line, section.line) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }
}
