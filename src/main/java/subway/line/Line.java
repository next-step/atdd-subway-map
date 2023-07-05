package subway.line;

import lombok.Builder;
import subway.line_station.LineStation;

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

    @Column(length = 100, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<LineStation> lineStations = new ArrayList<>();

    public Line() {
    }

    @Builder
    public Line(Long id, String name, String color, List<LineStation> lineStations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.lineStations = lineStations;
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
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

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public void addLineStation(LineStation lineStation) {
        lineStation.setLine(this);
        lineStations.add(lineStation);

    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
