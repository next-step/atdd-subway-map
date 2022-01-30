package nextstep.subway.domain;

import nextstep.subway.exception.IllegalUpdatingStateException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        checkPossibleAddingSection(section);
        section.updateLine(this);
        sections.add(section);
    }

    private void checkPossibleAddingSection(Section section) {
        if (sections.isEmpty()) {
            return;
        }
        if (!Objects.equals(getLastDownStation(), section.getUpStation())) {
            throw new IllegalUpdatingStateException("마지막 하행선이 요청한 구간의 상행선과 동일하지 않아 구간을 추가하지 못합니다.");
        }
        if (getAllStations().contains(section.getDownStation())) {
            throw new IllegalUpdatingStateException("요청한 구간의 하행역이 이미 노선에 등록되어있습니다.");
        }
    }

    private Station getLastDownStation() {
        if (sections.isEmpty()) {
            return null;
        }
        return sections.get(sections.size() - 1).getDownStation();
    }

    private List<Station> getAllStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Station> allStations = new ArrayList<>();
        allStations.add(sections.get(0).getUpStation());
        allStations.addAll(sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));
        return allStations;
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
}
