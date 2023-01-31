package subway.line;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.Station;

@Getter
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
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

    @Column(name = "DOWN_STATION_ID")
    private Long downStationId;

    @Column(name = "UP_STATION_ID")
    private Long upStationId;

    @Column(name = "DISTANCE")
    private Integer distance;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "LINE_ID", referencedColumnName = "LINE_ID")
    private List<Station> stationList;

    public Line(String name, String color, Long downStationId, Long upStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getStationList() {
        if (stationList == null) return new ArrayList<>();

        return stationList;
    }

    public void addStation(Station station){
        if (stationList == null) {
            stationList = new ArrayList<>();
        }

        station.changeLine(this.id);

        stationList.add(station);
    }
}
