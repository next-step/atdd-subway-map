package nextstep.subway.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.BadRequestException;

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

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
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

    public Line updateNameAndColor(final String name, final String color) {
        this.name = name;
        this.color = color;

        return this;
    }

    public void addSection(Section section) {
        this.sections.add(
                section.toBuilder()
                        .line(this)
                        .build());
    }

    public void deleteStation(Station station) {
        if (inValidSectionDelete()) {
            throw new BadRequestException("section can not delete");
        }

        Station lastStation = sections.get(getLastIndex()).getDownStation();

        if (!station.equals(lastStation)) {
            throw new BadRequestException("section can not delete");
        }

        sections.remove(getLastIndex());
    }

    private int getLastIndex() {
        return sections.size() - 1;
    }

    private boolean inValidSectionDelete() {
        return this.sections.size() < 2;
    }

}
