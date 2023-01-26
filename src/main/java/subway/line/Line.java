package subway.line;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import subway.station.Station;

@Entity
@Table(name = "LINE")
@NoArgsConstructor
@Getter
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "line")
    @BatchSize(size = 20)
    private List<Station> stations = new ArrayList<>();

    public Line(String name, String color) {
        this(name, color, new ArrayList<>());
    }

    public Line(String name, String color, List<Station> stations) {
        this.name = name;
        this.color = color;
        stations.forEach(this::addStation);
    }

    public void removeStation(Station station) {
        if (stations.contains(station)) {
            stations.remove(station);
            station.changeLine(null);
        }
    }

    public void addStation(Station station) {
        if (!stations.contains(station)) {
            stations.add(station);
            station.changeLine(this);
        }
    }
}
