package subway.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.sections.initSection(new Section(this, upStation, downStation, distance));
    }

    public Line updateLine(String name, String color) {
        this.name = name;
        this.color = color;

        return this;
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

    public Long getDistance() {
        return sections.getDistance();
    }

    public List<Section> getSections() {
        return sections.getValue();
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void removeSection(Long stationId) {
        sections.removeSection(stationId);
    }
}
