package subway.line.repository;

import subway.section.Section;
import subway.station.repository.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String color;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public Line() {}
    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.sections.add(section);
        section.updateLine(this);
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


        List<Station> stations = new ArrayList<>();
        stations.add(getUpStation());

        getSections().stream()
                .map(Section::getDownStation)
                .forEach(stations::add);

        return stations;
    }

    public List<Section> getSections() {
        return sections.stream()
                .sorted(Comparator.comparing(Section::getSequence))
                .collect(Collectors.toList());
    }

    public void updateName(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void appendSection(Section section) {

        Section lastSection = lastSection();

        if (!lastSection.getDownStation().equals(section.getUpStation())) {
            throw new IllegalArgumentException();
        }

        sections.add(section);
        section.updateLine(this);
    }

    public Section firstSection() {
        List<Section> sections = getSections();
        return sections.get(0);
    }
    public Section lastSection() {
        List<Section> sections = getSections();
        return sections.get(sections.size() - 1);
    }

    public Station getUpStation() {
        return firstSection().getUpStation();
    }

    public Station getDownStation() {
        return lastSection().getDownStation();
    }

}

