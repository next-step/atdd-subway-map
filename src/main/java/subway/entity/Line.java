package subway.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    private String color;
    @ManyToOne
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;
    @ManyToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;
    private int distance;

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }


    public List<Station> getStations() {
        return Arrays.asList(this.upStation, this.downStation);
    }

    public void modifyLine(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
