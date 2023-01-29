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

    public Line(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        this.name = name;
        this.color = color;
        sections.init(this, upStation, downStation, distance);
    }

    protected Line() {
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

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        sections.addSection(this, upStation, downStation, distance);
    }

    public void removeSection(final Station station) {
        sections.removeSection(station);
    }
}
