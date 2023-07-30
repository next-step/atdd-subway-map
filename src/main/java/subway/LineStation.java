package subway;

import javax.persistence.*;

@Entity
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    @Column()
    private Integer distance;

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getStation() {
        return station;
    }

    public Integer getDistance() {
        return distance;
    }

    public LineStation(Line line, Station station, Integer distance) {
        this.line = line;
        this.station = station;
        this.distance = distance;
    }

}
