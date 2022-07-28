package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Line {
    private static final String NOT_ALLOWED_EQUAL_STATIONS = "상행종점역과 하행종점역은 같을 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private int distance;

    @Embedded
    private Sections sections = new Sections();

    public Line(Long id, String name, String color, int distance, Station upStation, Station downStation) {
        this(name, color, distance, upStation, downStation);
        this.id = id;
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(NOT_ALLOWED_EQUAL_STATIONS);
        }

        sections.add(this, upStation, downStation, distance);

        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.add(this, upStation, downStation, distance);
    }

    public void removeSection(Station station) {
        sections.remove(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
