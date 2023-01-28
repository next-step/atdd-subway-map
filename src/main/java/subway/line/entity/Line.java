package subway.line.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.station.entity.Station;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Sections sections;

    @Builder
    public Line(String name, String color, List<Section> values) {
        this.name = name;
        this.color = color;
        this.sections = (values == null) ? new Sections() : Sections.from(values);
    }

    public static Line of(String name, String color) {
        return Line.builder()
                .name(name)
                .color(color)
                .build();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> stations() {
        return sections.stations();
    }

    public void addSection(Section section) {
        sections.add(section);
        section.changeLine(this);
    }

    public void removeLastSection() {

    }

    public void removeSection(long removeStationId) {
        long lastSectionDownStationId = sections.get(sections.size() - 1).getDownStation().getId();

        if (lastSectionDownStationId != removeStationId) {
            throw new IllegalArgumentException();
        }

        sections.remove(sections.size() - 1);
    }
}

