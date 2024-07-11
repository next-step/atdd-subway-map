package subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import subway.Station;

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
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
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

    public Line getUpdated(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }

    public void addSection(Section section) {
        sections.addSection(this, section);
    }

    public void removeSection(Station station) {
        sections.removeSection(station);
    }

    public List<Section> getSections() {
        return sections.toUnmodifiableList();
    }

    public Section getLastSection() {
        return sections.getLastSection();
    }
}