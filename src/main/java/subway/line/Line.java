package subway.line;

import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import subway.station.Station;

@Getter
@Entity
@Table(name = "LINE")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    @Column(name = "LINE_NAME", length = 20, nullable = false)
    private String name;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "LAST_DOWN_STATION_ID")
    private Integer lastDownStationId;

    @Column(name = "LAST_UP_STATION_ID")
    private Integer lastUpStationId;

    @Column(name = "DISTANCE")
    private Integer distance;

    @OneToMany
    @JoinColumn(name = "LINE_ID")
    private List<Station> stationList;
}
