package nextstep.subway.domain.line;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private Integer distance;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "line")
    private List<Section> sections = new ArrayList<>();


    @Builder(toBuilder = true)
    protected Line(Long id, String name, String color, Integer distance) {
        requireNonNull(name, "name is required");
        requireNonNull(color, "color is required");
        requireNonNull(distance, "distance is required");

        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public Set<Station> getStations() {
        return sections.stream()
                .map(section -> new Station[]{section.getUpStation(), section.getDownStation()})
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }
}
