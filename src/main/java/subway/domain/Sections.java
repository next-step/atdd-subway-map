package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import subway.exception.StationNotFoundException;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(final Section section) {
        sections.add(section);
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public void remove(final Section deleteSection) {
        sections.remove(deleteSection);
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
}