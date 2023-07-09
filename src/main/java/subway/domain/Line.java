package subway.domain;

import subway.ui.LineUpdateRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long distance;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
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

    public Long getDistance() {
        return distance;
    }

    public void update(LineUpdateRequest request) {
        this.name = request.getName();
        this.color = request.getColor();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public Set<Station> getStations() {
        return sections.getStations();
    }

    public void deleteSection(Long stationId) {
        sections.delete(stationId);
    }
}
