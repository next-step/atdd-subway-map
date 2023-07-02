package subway.line;

import subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany
    private List<Station> stations;

    public SubwayLine() {}

    public SubwayLine(String name, String color, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public List<Station> getStations() { return stations; }
}