package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
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

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        validate(section);

        sections.add(section);
        section.setLine(this);
    }

    private void validate(Section section) {
        sortSections();

        if (sections.isEmpty()) {
            return;
        }

        if (!sections.get(sections.size()-1).getDownStation().getId()
            .equals(section.getUpStation().getId())) {
            throw new IllegalArgumentException("등록하는 구간의 상행역이 기존 최하행역과 맞지 않음.");
        }

        if (sections.stream()
            .anyMatch(section1 -> (section1.getUpStation().getId()
                .equals(section.getDownStation().getId()))
            || section1.getDownStation().getId().equals(section.getDownStation().getId()))) {
            throw new IllegalArgumentException("등록하는 구간의 하행역이 기존 라인에 등록된 역임.");
        }
    }

    public List<Station> getStations() {
        sortSections();

        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < sections.size(); ++i) {
            stations.add(sections.get(i).getUpStation());
            if (i == sections.size()-1) {
                stations.add(sections.get(sections.size()-1).getDownStation());
            }
        }
        return stations;
    }

    private void sortSections() {

    }

    public boolean containsSection(Section section) {
        return sections.contains(section);
    }
}
