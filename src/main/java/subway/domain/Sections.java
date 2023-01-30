package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import subway.exception.SectionCannotAddException;
import subway.exception.SectionCannotRemoveException;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void initSection(Section section) {
        this.sections.add(section);
    }

    public void addSection(Section section) {
        validateAddSection(section);
        this.sections.add(section);
    }

    public void removeSection(Long stationId) {
        validateRemoveSection(stationId);
        var lastSection = sections.get(sections.size() - 1);
        sections.remove(lastSection);
    }

    public Long getDistance() {
        return sections.stream()
            .map(Section::getDistance)
            .reduce(0L, Long::sum);
    }

    public List<Section> getValue() {
        return sections;
    }

    private void validateAddSection(Section section) {
        Section lastSection = sections.get(sections.size() - 1);
        if (!lastSection.canAddSection(section)) {
            throw new SectionCannotAddException();
        }

        Optional<Station> matchedStation = sections.stream()
            .map(s -> List.of(s.getUpStation(), s.getDownStation()))
            .flatMap(Collection::stream)
            .filter(station -> station.isSameStation(section.getDownStation()))
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
        if (!lastSection.canRemoveSection(stationId)) {
            throw new SectionCannotRemoveException();
        }
    }
}
