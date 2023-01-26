package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @ManyToMany
    @JoinTable(name = "station_line",
            joinColumns = @JoinColumn(name = "line_id"),
            inverseJoinColumns = @JoinColumn(name = "station_id"))
    private List<Station> stations = new ArrayList<>();

    protected Line() {
    }

    public Line(final Long id, final String name, final String color, final List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new ArrayList<>());
    }

    public Line(final String name, final String color) {
        this(null, name, color, new ArrayList<>());
    }

    public static Line none() {
        return new Line();
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

    public List<Station> getStations() {
        return stations;
    }

    public void updateLine(final String changeName, final String changeColor) {
        this.name = changeName;
        this.color = changeColor;
    }

    public void addLine(final Station station) {
        this.stations.add(station);
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
