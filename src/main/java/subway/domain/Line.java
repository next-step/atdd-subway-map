package subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;

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
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance.getValue();
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
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
        this.downStation = station;
    }

    public boolean isNotEqualDownStation(final Station station) {
        return !this.downStation.equals(station);
    }

    public void canDeleteSection(final List<Section> sections, final Station station) {
        validateDeleteSectionEqualLineDownStation(this, station);
        validateLineContainMoreDefaultSection(this, sections);
    }

    private void validateDeleteSectionEqualLineDownStation(final Line line, final Station station) {
        if (line.isNotEqualDownStation(station)) {
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
