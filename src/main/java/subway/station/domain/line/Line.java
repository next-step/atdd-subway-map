package subway.station.domain.line;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.section.Section;
import subway.station.domain.section.Sections;
import subway.station.domain.station.Station;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    @Builder
    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, Long distance) {
        Section section = getSection(upStation, downStation, distance);
        section.assignLine(this);
        sections.addSection(section);
    }

    public void deleteSection(Station station) {
        sections.deleteSection(station);
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    private Section getSection(Station upStation, Station downStation, Long distance) {
        return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }


}
