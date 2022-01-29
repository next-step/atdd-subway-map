package nextstep.subway.domain;

import lombok.Builder;
import nextstep.subway.applicaion.object.Distance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Line() {
    }

    @Builder
    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        addSection(upStation, downStation, distance);
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

    public List<Section> getSections() { return sections; }

    public void addSection(Station upStation, Station downStation, Distance distance) {
        Section section = Section.builder()
                .line(this)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance.getValue())
                .build();
        sections.add(section);
    }

    public void update(String name, String color) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }

        if (color != null && !color.isEmpty()) {
            this.color = color;
        }
    }

    public Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public boolean equalsLastDownStation(Station upStation) {
        return getLastSection().getDownStation().equals(upStation);
    }

    public boolean checkDuplicatedDownStation(Station downStation) {
        boolean duplicatedDownStationYn = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet())
                .contains(downStation);
        boolean duplicatedUpStationYn = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toSet())
                .contains(downStation);
        return duplicatedDownStationYn || duplicatedUpStationYn;
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .sorted(Comparator.comparing(Section::getId))
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(getLastSection().getDownStation());
        return stations;
    }
}
