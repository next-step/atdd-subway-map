package subway.domain.line;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.station.Station;
import subway.domain.station.Stations;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;
    @Embedded
    private Stations stations;

    private Integer distance;


    @Builder
    public Line(String name, String color, Stations stations, Integer distance) {
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;

        stations.belongTo(this);
    }

    public List<Station> getStationList() {
        return stations.getStationList();
    }
}