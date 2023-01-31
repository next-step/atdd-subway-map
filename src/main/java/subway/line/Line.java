package subway.line;

import lombok.Getter;
import subway.station.Station;

import javax.persistence.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

//    @ManyToMany
//    @JoinTable(name = "line_stations")
//    private List<Station> stations = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

//    public void appendSection(Station newStation) {
//        this.stations.add(newStation);
//    }
}
