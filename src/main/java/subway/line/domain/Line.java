package subway.line.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Stations stations;

    @Column(length = 10, nullable = false)
    private Integer distance;

    @OneToMany(mappedBy = "line", fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(Long id, String name, String color, Integer distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public static Line of(Long id, String name, String color, Integer distance) {
        return new Line(id, name, color, distance);
    }

    public void saveStations(Stations stations) {
        this.stations = stations;
    }

    public void saveSections(List<Section> sections) {
        this.sections = sections;
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

    public Stations getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line route = (Line) o;

        if (!Objects.equals(id, route.id)) return false;
        return Objects.equals(name, route.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
