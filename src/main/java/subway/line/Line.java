package subway.line;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    public Long getDistance() {
        return distance;
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    @Column(nullable = false)
    private Long distance = 0L;

    @OneToMany(mappedBy = "line")
    private List<LineStation> lineStations;

    protected Line() {
    }

    public Line(final String name, final String color, final Long distance, final List<LineStation> lineStations) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.lineStations = lineStations;
    }

    public Line(final String name, final String color, final Long distance) {
        this(name, color, distance, new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return this.color;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
