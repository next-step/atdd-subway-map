package nextstep.subway.domain;

import nextstep.subway.applicaion.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        if (!validateUpStation(section.getUpStation())) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        sections.add(section);

        if (section.getLine() != this) {
            section.changeLine(this);
        }
    }

    public boolean validateUpStation(Station upStation) {
        if (this.sections.isEmpty()) {
            return true;
        }

        Station lastDownStation = sections
            .stream()
            .map(Section::getDownStation)
            .reduce((a, b) -> b)
            .orElseThrow(() -> new IllegalArgumentException("하행 종점역이 없습니다."));

        return lastDownStation == upStation;
    }
}
