package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color);
        addSection(upStation, downStation, distance);
    }

    public Section addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(this, upStation, downStation, distance);

        if (isEmptySections()) {
            this.sections.add(section);
            return section;
        }

        if (isNotEndDownStation(section.getUpStation())) {
            throw new IllegalArgumentException("등록할 구간의 상행역은 해당 노선의 하행 종점역이어야 합니다.");
        }

        if (isAlreadyRegisteredStation(section.getDownStation())) {
            throw new IllegalArgumentException("등록할 구간의 하행역은 해당 노선에 등록되어 있지 않아야 합니다.");
        }

        this.sections.add(section);
        return section;
    }

    public List<Station> getFlatStations() {
        if (isEmptySections()) {
            return new ArrayList<>();
        }

        Set<Station> stations = new LinkedHashSet<>();

        sections.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });

        return new ArrayList<>(stations);
    }

    private boolean isAlreadyRegisteredStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isExistAnyStation(station));
    }

    private boolean isNotEndDownStation(Station station) {
        return !getEndDownStation().equals(station);
    }

    private boolean isEmptySections() {
        return sections.isEmpty();
    }

    private Station getEndDownStation() {
        return getLastSection().getDownStation();
    }

    private Section getLastSection() {
        if (isEmptySections()) { //Defensive
            throw new IllegalArgumentException("section이 존재하지 않습니다.");
        }

        int sectionLastIndex = sections.size() - 1;

        return sections.get(sectionLastIndex);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections;
    }

    public String getColor() {
        return color;
    }

    public Line update(Line updateLine) {
        this.name = updateLine.getName();
        this.color = updateLine.getColor();

        return this;
    }
}
