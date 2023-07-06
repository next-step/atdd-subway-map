package subway.line_station;

import lombok.Builder;
import subway.line.Line;
import subway.station.Station;

import javax.persistence.*;

@Entity
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(nullable = false)
    private Long stationOrder;

    @Column(nullable = true)
    private Long nextDistance;

    public static final Long lastDistance = 0L;

    public LineStation() {
    }

    @Builder
    public LineStation(Long id, Station station, Long stationOrder, Long nextDistance) {
        this.id = id;
        this.station = station;
        this.stationOrder = stationOrder;
        this.nextDistance = nextDistance;
    }

    public Station getStation() {
        return station;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
