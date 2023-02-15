package subway.domain;

import java.util.List;
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
    private Sections sections;

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
        this.sections = new Sections(new Section(distance, upStation, downStation, this));
        this.distance = new Distance(distance);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Station> getStations() {
        return List.of(sections.getLineUpStation(), sections.getLineDownStation());
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance.getValue();
    }

    public Station getDownStation() {
        return sections.getLineDownStation();
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

    public void deleteBy(final Station station) {
        if (!getDownStation().equals(station)) {
            throw new SectionConstraintException();
        }
        this.distance.minus(sections.deleteBy(station));
    }

    public void addSection(final int distance, final Station upStation, final Station downStation) {
        this.sections.add(new Section(distance, upStation, downStation, this));
    }
}
