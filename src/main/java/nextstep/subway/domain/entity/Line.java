package nextstep.subway.domain.entity;

import nextstep.subway.domain.service.Validator;

import javax.persistence.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(final String name,
                final String color,
                final Station upStation,
                final Station downStation,
                final int distance,
                final Validator<Line> lineValidator) {
        this.name = name;
        this.color = color;

        lineValidator.validate(this);

        addSection(new Section(this, upStation, downStation, distance));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        final Map<Station, Station> stationMap = sections.stream().collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        final Set<Station> upStations = stationMap.keySet();
        final Set<Station> downStations = new HashSet<>(stationMap.values());

        final List<Station> stations = new ArrayList<>();
        Station nextStation = upStations.stream()
                .filter(Predicate.not(downStations::contains))
                .findAny().orElseThrow(IllegalStateException::new);
        while (upStations.contains(nextStation)) {
            stations.add(nextStation);
            nextStation = stationMap.get(nextStation);
        }
        stations.add(nextStation);
        return stations;
    }

    public void change(final String name, final String color, final Validator<Line> lineValidator) {
        this.name = name;
        this.color = color;

        lineValidator.validate(this);
    }

    public void addSection(final Section section) {
        this.sections.add(section);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
