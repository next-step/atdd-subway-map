package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color);
        addSection(upStation, downStation, distance);
    }

    public Section addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(this, upStation, downStation, distance);
        return sections.add(section);
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public List<Station> getStations() {
        return sections.getFlatStations();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Line update(Line updateLine) {
        this.name = updateLine.getName();
        this.color = updateLine.getColor();

        return this;
    }
}
