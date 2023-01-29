package subway.line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;

    @Builder.Default
    @ManyToMany
    private List<Station> stations = new ArrayList<>();

    public void addStation(Station station) {
        this.stations.add(station);
    }
    public void updateName(String name) {
        this.name = name;
    }
    public void updateColor(String color) {
        this.color = color;
    }
}
