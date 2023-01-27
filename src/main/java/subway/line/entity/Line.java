package subway.line.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.station.entity.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
//    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<Section> values = new ArrayList<>();

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

//    private List<Station> createStations() {
//        List<Station> stations = values.stream()
//                .map(e -> e.getUpStation())
//                .collect(Collectors.toList());
//
//        stations.add(lastSection().getDownStation());
//        return stations;
//    }
//
//    private Section lastSection() {
//        return values.get(values.size() - 1);
//    }
//
    public void addSection(Section section) {
        if (sections.contains(section)) {
            return;
        }

        sections.add(section);
        section.changeLine(this);
    }
}

