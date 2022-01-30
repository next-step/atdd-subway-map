package nextstep.subway.domain;

import nextstep.subway.exception.IllegalUpdatingStateException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (Objects.equals(getLastDownStation(), section.getUpStation())) {
            return;
        }
        throw new IllegalUpdatingStateException("마지막 하행선이 요청한 구간의 상행선과 동일하지 않아 구간을 추가하지 못합니다.");
    }

    private Station getLastDownStation() {
        if (sections.isEmpty()) {
            return null;
        }
        return sections.get(sections.size() - 1).getDownStation();
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
}
