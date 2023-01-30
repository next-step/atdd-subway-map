package subway.line;

import subway.*;

import javax.persistence.*;

@Entity
public class LineStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "station_order", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    private Integer distance;

    protected LineStation() {
    }

    public LineStation(final Integer order, final Station station, final Integer distance) {
        this.order = order;
        this.station = station;
        this.distance = distance;
    }

    public void changeLine(final Line line) {
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Integer getOrder() {
        return order;
    }

    public Station getStation() {
        return station;
    }

    public Line getLine() {
        return line;
    }

    public Integer getDistance() {
        return distance;
    }
}
