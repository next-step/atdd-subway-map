package nextstep.subway.domain;

import nextstep.subway.exception.ContainStationException;
import nextstep.subway.exception.NotEqualDownStationException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Embedded
    private final Sections sections = new Sections();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    protected Line() {
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.sections.addSection(section);
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

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Sections getSections() {
        return sections;
    }

    public void addSection(Section section) {
        checkNotEqualDownStation(section);
        checkContainSection(section);
        sections.addSection(section);
    }

    private void checkNotEqualDownStation(Section section) {
        Section lastSection = sections.getSections().get(sections.getSections().size() - 1);
        if (lastSection.getDownStation() != section.getUpStation()) {
            throw new NotEqualDownStationException();
        }
    }

    private void checkContainSection(Section section) {
        if (getStations().contains(section.getDownStation())) {
            throw new ContainStationException();
        }
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        sections.getSections().stream()
                .forEach(section -> {
                    stations.add(section.getDownStation());
                    stations.add(section.getUpStation());
                });
        return stations.stream().distinct().collect(Collectors.toList());
    }
}
