package subway.line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import subway.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
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
    @OneToMany
    private List<Station> stations = new ArrayList<>();

    public void addStation(Station station) {
        this.stations.add(station);
    }
}
