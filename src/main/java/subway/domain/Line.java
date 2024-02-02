package subway.domain;

import subway.exception.LineSectionException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Line {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    protected Line() {}

    public static Line create(String name, String color) {
        return new Line(name, color);
    }

    public void update(String name, String color) {
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

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getAllStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public Station getTheMostDownStation() {
        Section theMostDownSection = sections.get(sections.size() - 1);
        return theMostDownSection.getDownStation();
    }

    public void deleteStation(Long stationId) {
        if (sections.size() <= 1) {
            throw new LineSectionException();
        }
        if (getTheMostDownStation().getId() != stationId) {
            throw new LineSectionException();
        }
        sections.remove(sections.size() - 1);
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
        } else if (isAvailableDownStation(section.getDownStation()) && isAvailableUpStation(section.getUpStation())) {
            sections.add(section);
        } else {
            throw new LineSectionException("안됨");
        }
    }

    private Boolean isAvailableDownStation(Station downStation) {
        return !getAllStations().contains(downStation);
    }

    private Boolean isAvailableUpStation(Station upStation) {
        Section lastSection = sections.get(sections.size() - 1);
        return lastSection.getDownStation().equals(upStation);
    }
}
