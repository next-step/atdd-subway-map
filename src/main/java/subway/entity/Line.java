package subway.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.enums.SubwayMessage;
import subway.exception.SubwayException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public List<Section> getSections() {
        return sections.getSections();
    }
    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }
    public List<Station> getStations() {
        return sections.getStations();
    }

    public void modifyLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
       sections.add(this, upStation, downStation, distance);
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }
}
