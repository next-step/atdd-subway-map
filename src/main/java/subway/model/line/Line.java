package subway.model.line;

import lombok.*;
import subway.model.section.Section;
import subway.model.station.Station;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "line", cascade = CascadeType.ALL) // TODO: lazy vs eager. TODO: N+1문제 확인
    List<Section> sections;

    @Column(nullable = false)
    private Long distance;

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Station> getStations() {

        List<Station> stations = this.sections.stream()
                                             .map(Section::getUpStation)
                                             .collect(Collectors.toList());
        stations.add(getLastStation());

        return stations;
    }

    public Station getLastStation() {
        return this.sections.get(this.sections.size() - 1)
                            .getDownStation();
    }
}
