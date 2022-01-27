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
        validateSection(section);
        this.sections.add(section);
    }

    private void validateSection(final Section section) {
        if (validateUpStation(section.getUpStation()) || validateDownStation(section.getDownStation())) {
            throw new IllegalArgumentException();
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
