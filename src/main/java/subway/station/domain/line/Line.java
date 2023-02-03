package subway.station.domain.line;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String color;

    @Column
    private Long upStationId;

    @Column
    private Long downStationId;

    @Column
    private Long distance;

    @OneToMany
    @Column
    private List<Station> stations;

    public Line() {
    }

    @Builder
    public Line(String name, String color, Long upStationId, Long downStationId, Long distance, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.stations = stations;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
