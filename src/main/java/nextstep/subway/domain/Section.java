package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @ManyToOne
    public Station upStation;

    @ManyToOne
    public Station downStation;

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(long distance, Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("상행종점역과 하행종점역의 아이디는 같을 수 없습니다.");
        }
        this.distance = new Distance(distance);
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }

    public boolean matchDownStation(Section section) {
        return downStation.equals(section.upStation);
    }

    public boolean matchStation(Section section) {
        return getStations().contains(section.downStation);
    }

    public boolean isSameDownStation(Station station) {
        return downStation.equals(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }
}
