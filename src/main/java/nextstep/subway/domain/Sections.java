package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    private static final int EMPTY_SIZE = 0;
    private static final int GAP_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(final Section section) {
        validateAddSection(section);
        this.sections.add(section);
    }

    public void remove(final Station station) {
        validateRemoveSection(station);
        this.sections.remove(sections.get(sections.size() - GAP_SIZE));
    }

    private void validateRemoveSection(final Station station) {
        if (sections.size() <= 1 || !sections.get(sections.size() - GAP_SIZE).isDownStation(station)) {
            throw new IllegalArgumentException("invalid station occurred");
        }
    }

    private void validateAddSection(final Section section) {
        if (validateUpStation(section.getUpStation()) || validateDownStation(section.getDownStation())) {
            throw new IllegalArgumentException("invalid station occurred");
        }
    }

    private boolean validateUpStation(final Station upStation) {
        if (sections.size() == EMPTY_SIZE) {
            return false;
        }
        final Section last = sections.get(sections.size() - GAP_SIZE);
        return !last.isDownStation(upStation);
    }

    private boolean validateDownStation(final Station downStation) {
        return sections.stream()
                .anyMatch(it -> it.isAnyStation(downStation));
    }
}
