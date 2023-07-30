package subway;

import javax.persistence.*;

@Entity
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @Column()
    private Integer distance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public LineStation() {

    }

    public LineStation(Long id, Line line, Station station, Integer distance) {
        this.id = id;
        this.line = line;
        this.station = station;
        this.distance = distance;
    }

    public LineStation(Line line, Station station, Integer distance) {
        this.line = line;
        this.station = station;
        this.distance = distance;
    }

}
