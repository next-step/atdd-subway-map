package subway.station.domain.line;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.section.Section;
import subway.station.domain.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
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

    @OneToMany(mappedBy = "line", orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    @Builder
    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());
        sections
                .forEach(section -> {
                    stations.add(section.getDownStation());
                });

        return stations;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
