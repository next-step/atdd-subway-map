package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    public static final int SECTIONS_MIN_SIZE = 1;
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

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        section.setLine(this);
        this.sections.add(section);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        verifyConnectable(section);
        section.setLine(this);
        sections.add(section);
    }

    public void removeSection(Station station) {
        Section lastSection = sections.get(sections.size() - 1);

        if (!lastSection.isEqualToDownStation(station) || sections.size() == SECTIONS_MIN_SIZE) {
            throw new IllegalArgumentException("구간을 제거할 수 없습니다.");
        }

        sections.remove(lastSection);
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        Section lastSection = sections.get(sections.size() - 1);
        stations.add(lastSection.getDownStation());

        return stations;
    }

    private void verifyConnectable(Section section) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("기존 구간이 없습니다.");
        }
        if (getStations().contains(section.getDownStation())) {
            throw new IllegalArgumentException("기등록된 역은 하행역으로 등록할 수 없습니다.");
        }

        Section lastSection = sections.get(sections.size() - 1);
        if (!lastSection.isConnectable(section)) {
            throw new IllegalArgumentException("상행역이 기등록된 하행 종점역과 일치하지 않습니다.");
        }
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
