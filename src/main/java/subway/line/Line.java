package subway.line;

import subway.section.Section;
import subway.section.SectionCannotDeleteException;
import subway.section.Sections;
import subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(List.of(new Section(this, upStation, downStation, distance)));
    }

    public void createSection(Section section) {
        sections.createSection(section);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void deleteSection(Long downStationId) {
        if (sections.getSections().size() < 2) {
            throw new SectionCannotDeleteException("If there is less than one registered section, you cannot delete it.");
        }

        sections.deleteSection(downStationId);
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

    public Sections getSections() {
        return sections;
    }
}
