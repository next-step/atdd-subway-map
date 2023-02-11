package subway.line;

import subway.common.LineSectionValidator;
import subway.section.Section;
import subway.section.Sections;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return new Sections(sections).getAllStation();
    }

    public Line addSection(Station upStation, Station downStation, Long distance) {
        var newSection = new Section(this, upStation, downStation, distance);
        if (!sections.isEmpty()) {
            LineSectionValidator.validate(this, newSection);
        }
        sections.add(newSection);
        return this;
    }

    public void deleteSection(Station downStation) {
        var lineSection = new Sections(sections);
        LineSectionValidator.deleteValidate(lineSection, downStation);
        sections.remove(lineSection.getLastSection());
    }

    public Line update(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }
}
