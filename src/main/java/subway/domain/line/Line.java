package subway.domain.line;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;
    @Embedded
    private Sections sections;

    private Integer distance;


    @Builder
    public Line(String name, String color, Sections sections, Integer distance) {
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.distance = distance;

        sections.belongTo(this);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        sections.addSection(section);
        section.belongTo(this);
    }

    public void deleteSection(Section section) {
        sections.deleteSection(section);
    }

    public Section getSection(Station station) {
        return sections.getSection(station);
    }
}