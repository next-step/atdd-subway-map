package subway.line;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int upStationId;
    private int downStationId;
    private int distance;
    @OneToMany
    @JoinColumn(name = "station_id")
    private List<Station> stations;

    @Builder
    public Line(String name, String color, int upStationId, int downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
