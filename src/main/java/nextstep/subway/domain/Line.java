package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.enums.Direction;
import nextstep.subway.exception.InvalidSectionException;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(Long id, LineRequest lineRequest) {
        return new Line(id, lineRequest.getName(), lineRequest.getColor());
    }

    public static Line of(LineRequest lineRequest) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor()
        );
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

    public List<Section> getSections() {
        return sections;
    }

    public boolean addSection(Section section) {
        // section 이 상행종점인지, 하행 종목인지를 구분한다.
        Direction direction = getStations()
                                .getDirection(section);
        if (direction.equals(Direction.NEW)
        || (direction.equals(Direction.DOWN) && validateDownEnd(section.getUpStation()))
        || (direction.equals(Direction.UP) && validateUpEnd(section.getDownStation()))) {
            return this.sections.add(section);
        }

        return false;
    }

    private boolean validateUpEnd(Station downStation) {
        boolean a = sections.stream()
                .filter(section -> section.getUpStation().equals(downStation))
                .count() == 1L;
        boolean b = sections.stream()
                .noneMatch(section -> section.getDownStation().equals(downStation));
        if (!a || !b) {
            throw new InvalidSectionException();
        }

        return true;
    }

    private boolean validateDownEnd(Station upStation) {
        boolean a = sections.stream()
                .filter(section -> section.getDownStation().equals(upStation))
                .count() == 1L;
        boolean b = sections.stream()
                .noneMatch(section -> section.getUpStation().equals(upStation));

        if (!a || !b) {
            throw new InvalidSectionException();
        }

        return true;
    }

    public Stations getStations() {
        Set<Station> stations = new HashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }

        return Stations.of(stations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }
}
