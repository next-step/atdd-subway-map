package subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.StringUtils;
import subway.exception.SectionConstraintException;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Embedded
    private Stations stations;

    @Embedded
    private Distance distance;

    protected Line() {
    }

    public Line(
            final String name,
            final String color,
            final Station upStation,
            final Station downStation,
            final int distance
    ) {
        this.name = name;
        this.color = color;
        this.stations = new Stations(upStation, downStation);
        this.distance = new Distance(distance);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Station> getStations() {
        return List.of(stations.getUpStation(), stations.getDownStation());
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance.getValue();
    }

    public Station getUpStation() {
        return stations.getUpStation();
    }

    public Station getDownStation() {
        return stations.getDownStation();
    }

    public void modify(final String name, final String color, final int distance) {
        editName(name);
        editColor(color);
        editDistance(distance);
    }

    private void editName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
        this.name = name;
    }

    private void editColor(final String color) {
        if (!StringUtils.hasText(color)) {
            throw new IllegalArgumentException("색상은 공백일 수 없습니다.");
        }
        this.color = color;
    }

    private void editDistance(final int distance) {
        this.distance = new Distance(distance);
    }

    public void updateDownStation(final Station station) {
        stations.updateDownStation(station);
    }

    public boolean equalDownStation(final Station station) {
        return this.stations.equalDownStation(station);
    }

    public void canDeleteSection(final List<Section> sections, final Station station) {
        validateDeleteSectionEqualLineDownStation(this, station);
        validateLineContainMoreDefaultSection(this, sections);
    }

    private static void validateDeleteSectionEqualLineDownStation(final Line line, final Station station) {
        if (!line.equalDownStation(station)) {
            throw new SectionConstraintException();
        }
    }

    private void validateLineContainMoreDefaultSection(final Line line, final List<Section> sections) {
        List<Station> stations = sections.stream()
                .map(Section::getStation)
                .collect(Collectors.toUnmodifiableList());

        if (stations.equals(line.getStations())) {
            throw new SectionConstraintException();
        }
    }
}
