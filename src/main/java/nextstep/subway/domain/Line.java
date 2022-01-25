package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

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

    public List<Section> getSections() {
        return sections;
    }

    public void changeLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
    }

    public boolean stationNoneMach(Station downStation) {
        Set<Station> stations = new HashSet<>();
        sections.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });
        return stations
                .stream()
                .anyMatch(station -> station.getName().equals(downStation.getName()));
    }
}
