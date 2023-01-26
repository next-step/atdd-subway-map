package subway.domain;

import javax.persistence.*;

@Entity(name = "station_line")
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "line_id")
    private Long lineId;

    @Column(name = "station_id")
    private Long stationId;

    protected StationLine() {
    }
}
