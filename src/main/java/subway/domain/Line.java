package subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "LINE")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String name;
    @Column(length = 20)
    private String color;
    @Embedded
    private final Sections sections = new Sections();

    private Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    protected Line() {
    }

    public static Line of(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        final Line line = new Line(name, color);
        line.sections.init(line, upStation, downStation, distance);
        return line;
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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void update(final String name, final String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
    }

    public void addSection(final SectionValidator sectionValidator, final Station upStation, final Station downStation, final int distance) {
        sections.addSection(sectionValidator, this, upStation, downStation, distance);
    }

    public void removeSection(final SectionValidator sectionValidator, final Station station) {
        sections.removeSection(sectionValidator, station);
    }
}
