package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;

import javax.persistence.*;
import java.util.Collection;

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
    private Sections sections = new Sections();


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


    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Collection<Long> getStationIds() {
        return sections.getStationIds();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeLastStation(Long stationId) {
        sections.removeLastStation(stationId);
    }

}
