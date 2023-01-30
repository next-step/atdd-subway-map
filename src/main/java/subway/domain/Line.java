package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import subway.exception.SectionCannotAddException;
import subway.exception.SectionCannotRemoveException;

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
        validateAddSection(section);
        sections.add(section);
    }

    public void removeSection(Long stationId) {
        validateRemoveSection(stationId);
        var lastSection = sections.stream()
            .filter(section -> !Objects.equals(section.getDownStation().getId(), stationId))
            .findFirst().orElseThrow();

        sections.remove(lastSection);
    }

    private void validateAddSection(Section section) {
        Section lastSection = sections.get(sections.size() - 1);
        if (!Objects.equals(lastSection.getDownStation().getId(), section.getUpStation().getId())) {
            throw new SectionCannotAddException();
        }

        Optional<Station> matchedStation = sections.stream()
            .map(s -> List.of(s.getUpStation(), s.getDownStation()))
            .flatMap(Collection::stream)
            .filter(station -> Objects.equals(station.getId(), section.getDownStation().getId()))
            .findAny();

        if (matchedStation.isPresent()) {
            throw new SectionCannotAddException();
        }
    }

    private void validateRemoveSection(Long stationId) {
        if (sections.size() <= 1) {
            throw new SectionCannotRemoveException();
        }

        Section lastSection = sections.get(sections.size() - 1);
        if (!Objects.equals(lastSection.getDownStation().getId(), stationId)) {
            throw new SectionCannotRemoveException();
        }
    }
}
