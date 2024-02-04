package subway.domain.line.entity;

import subway.domain.station.entity.Station;

import javax.persistence.*;
import java.security.InvalidParameterException;
import java.util.Objects;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Station upStation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Station downStation;

    private Long distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    public Section(){}
    public Section(Station upStation, Station downStation, Long distance) {
        if(upStation.equals(downStation)) {
            throw new InvalidParameterException();
        }
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(getUpStation(), section.getUpStation()) && Objects.equals(getDownStation(), section.getDownStation()) && Objects.equals(distance, section.distance) && Objects.equals(line, section.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getUpStation(), getDownStation(), distance, line);
    }

    @Override
    public String toString() {
        return "Section{" +
                " upStation=" + upStation.getId() +
                ", downStation=" + downStation.getId() +
                ", distance=" + distance +
                '}';
    }
}
