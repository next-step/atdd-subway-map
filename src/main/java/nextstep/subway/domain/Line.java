package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    private Integer distance;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Integer distance) {
        this.name = name;
        this.color = color;
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public void edit(String name, String color, Integer distance) {
        if (name != null && !name.isBlank()) {
            System.out.println("name = " + name);
            this.name = name;
        }
        if (color != null && !color.isBlank()) {
            this.color = color;
        }
        if (distance != null) {
            this.distance = distance;
        }
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

    public Integer getDistance() {
        return distance;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public List<Long> getAllStationId() {
        return sections.getAllStationId();
    }

    public List<Station> getAllStation() {
        return sections.getStations();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
