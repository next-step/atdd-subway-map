package subway.domain.line;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.station.Station;
import subway.domain.station.Stations;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    public List<Station> getStations() {
        return stations.getStations();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}