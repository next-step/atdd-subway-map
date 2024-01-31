package subway.line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.Section;
import subway.section.Sections;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String name;

//    @ManyToOne
//    private Station upstation;
//
//    @ManyToOne
//    private Station downstation;

    @Embedded
    private Sections sections = new Sections();

    @Column(nullable = false)
    private int distance;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void initSection(Station upstation, Station downstation) {
        Section section = Section.initSection(this, upstation, downstation);
        sections.initSection(section);
    }

    public void addSection(Section section) {
        sections.addSection(section);
        distance += section.getDistance();
    }

    public void popSection(Station station) {
        int lastSectionDistance = sections.getLastSectionDistance();
        distance -= lastSectionDistance;
        sections.popSection(station);
    }

    @Builder
    public Line(Long id, String color, String name, Sections sections, int distance) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.sections = sections != null ? sections : new Sections();
        this.distance = distance;
    }
}
