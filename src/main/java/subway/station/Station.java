package subway.station;

import javax.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "STATION")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATION_ID")
    private Long id;

    @Column(name = "LINE_ID")
    private Long lineId;

    @Column(name = "STATION_NAME", length = 20, nullable = false)
    private String name;

    public Station() {}

    public Station(Long lineId, String name) {
        this.lineId = lineId;
        this.name = name;
    }
}
