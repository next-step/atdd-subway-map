package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@NoArgsConstructor
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> stations() {
        Stream<Station> upStations = sections.stream().map(Section::getUpStation);
        Stream<Station> downStations = sections.stream().map(Section::getDownStation);
        return Stream.concat(upStations, downStations)
                .distinct()
                .sorted(Comparator.comparing(Station::getId))
                .collect(Collectors.toList());
    }

    public boolean isDownStation(Station station) {
        return downStation().equals(station);
    }

    private Station downStation() {
        List<Station> stations = stations();
        return stations.get(stations.size() - 1);
    }

    public boolean has(Station station) {
        return stations().contains(station);
    }

    public boolean hasAnyStation() {
        return !sections.isEmpty();
    }
}
