package nextstep.subway.domain;

import nextstep.subway.error.exception.InvalidValueException;

import javax.persistence.*;
import java.util.ArrayList;
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
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void validateNewSection(Long upStationId, Long downStationId) {
        if (!sections.isEmpty() &&
                doesNotMatchLastDownStation(upStationId) ||
                doesContains(downStationId)) {
            throw new InvalidValueException();
        }
    }

    public void sortedSections() {
        Section firstSection = getFirstSection();
        sections = new Sections(sections).sorted(firstSection);
    }

    private Section getFirstSection() {
        return sections.stream()
                .filter(us ->
                        sections.stream().noneMatch(ds ->
                                ds.getDownStation().equals(us.getUpStation())))
                .collect(Collectors.toList())
                .get(0);
    }

    private Station getLastDownStation() {
        return sections.stream()
                .filter(ds -> sections.stream().noneMatch(us ->
                        us.getUpStation().equals(ds.getDownStation())))
                .collect(Collectors.toList())
                .get(0)
                .getDownStation();
    }

    private boolean doesContains(Long downStationId) {
        return sections.stream().anyMatch(s ->
                downStationId.equals(s.getUpStation().getId()) ||
                downStationId.equals(s.getDownStation().getId()));
    }

    private boolean doesNotMatchLastDownStation(Long upStationId) {
        return !upStationId.equals(getLastDownStation().getId());
    }

    public void addSection(Section section) {
        if (!sections.contains(section)) {
            sections.add(section);
        }
        section.applyToLine(this);
    }

    public Section findSection(Long stationId) {
        return sections.stream()
                .filter(s -> s.getDownStation().getId().equals(stationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public void validatePossibleToRemove(Section section) {
        if (sections.size() <= 1 ||
                !section.getDownStation().equals(getLastDownStation())) {
            throw new InvalidValueException(section.getId());
        }
    }

    public List<Station> getStations() {
        sortedSections();

        List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());
        sections.forEach(s -> stations.add(s.getDownStation()));

        return stations;
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

    public void edit(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
