package subway.line;

import subway.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    public Line() {}

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getColor() { return color; }

    public List<Section> getSections() { return this.sections; }

    public void addSection(Section section) {
        sections.add(section);
    }

    public boolean deleteSectionByStation(Station station) {
        List<Section> sections = getSections();
        Section lastSection = sections.get(sections.size() -1);

        if (lastSection.getDownStation().getId().equals(station.getId())) {
            sections.remove(lastSection);
            return true;
        }
        return false;
    }
}