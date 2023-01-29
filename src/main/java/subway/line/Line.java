package subway.line;

import lombok.Getter;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "lines")
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @ManyToMany
    @JoinTable(name = "line_stations")
    private List<Station> stations = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.stations.add(upStation);
        this.stations.add(downStation);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
