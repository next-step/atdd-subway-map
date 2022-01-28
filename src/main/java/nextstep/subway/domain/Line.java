package nextstep.subway.domain;

import nextstep.subway.domain.exception.LineException;
import nextstep.subway.domain.exception.SectionException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int distance;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public Station getUpStation() {
        return sections.isEmpty() ? null : sections.get(sections.size() - 1).getUpStation();
    }

    public Station getDownStation() {
        return sections.isEmpty() ? null : sections.get(sections.size() - 1).getDownStation();
    }

    public int getDistance() {
        return distance;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public void extend(Section section) {
        if (!section.isStartWith(getDownStation())) {
            throw SectionException.ofIllegalUpStation(section);
        }

        if (isComposedOf(section.getDownStation())) {
            throw SectionException.ofIllegalDownStation(section);
        }

        sections.add(section);
    }

    public void shorten(Station station) {
        if (!isDownStation(station)) {
            throw SectionException.ofIllegalDownStation(station);
        }

        if (hasOnlyOneSection()) {
            throw LineException.ofSectionSize();
        }
    }

    private boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

    private boolean isDownStation(Station station) {
        return station.equals(getDownStation());
    }

    private boolean isComposedOf(Station station) {
        return sections.stream().anyMatch(section -> section.isStartWith(station) || section.isEndWith(station));
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(getDownStation());
        return stations.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
