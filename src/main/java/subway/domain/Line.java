package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line updateLine(String name, String color) {
        this.name = name;
        this.color = color;

        return this;
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

    public Long getDistance() {
        return sections.stream()
            .map(Section::getDistance)
            .reduce(0L, Long::sum);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeSection(Long stationId) {
        var lastSection = sections.stream()
            .filter(section -> !Objects.equals(section.getDownStation().getId(), stationId))
            .findFirst().orElseThrow();

        sections.remove(lastSection);
    }
}
