package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import subway.exception.SectionConstraintException;
import subway.exception.StationNotFoundException;

@Embeddable
public class Sections {

    private static final int MIN = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(final Section section) {
        sections.add(section);
    }

    public void add(final Section section) {
        validateAddSection(section);
        sections.add(section);
    }

    private void validateAddSection(Section section) {
        if (!sections.isEmpty() && (isLineDownStation(section) || isAlreadyExistsSection(section.getDownStation()))) {
            throw new SectionConstraintException();
        }
    }

    private boolean isLineDownStation(final Section section) {
        return !section.getUpStation().equals(getDownStation());
    }

    private boolean isAlreadyExistsSection(final Station station) {
        return sections.stream().anyMatch(s -> s.contain(station));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public void remove(final Section deleteSection) {
        deleteBy(deleteSection.getDownStation());
    }

    public Station getDownStation() {
        return sections.stream()
                .map(Section::getDownStation)
                .filter(station -> !this.getBasedOnUpStationBy(station))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    private boolean getBasedOnUpStationBy(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualUpStation(station));
    }

    public Station getUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .filter(station -> !this.getBasedOnDownStationBy(station))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    private boolean getBasedOnDownStationBy(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualDownStation(station));
    }

    public Distance deleteBy(final Station station) {
        if (sections.size() <= MIN) {
            throw new SectionConstraintException();
        }
        Section section = sections.stream()
                .filter(s -> s.isEqualDownStation(station))
                .findAny()
                .orElseThrow(SectionConstraintException::new);

        sections.remove(section);
        return section.getDistance();
    }
}
