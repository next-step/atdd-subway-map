package nextstep.subway.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private Set<Section> sections = new LinkedHashSet<>();

    protected Sections() {
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public List<Station> stations() {
        return sections.stream()
            .map(Section::stations)
            .flatMap(List::stream)
            .distinct()
            .sorted(Comparator.comparingLong(Station::getId))
            .collect(Collectors.toUnmodifiableList());
    }


    public void removeLastSection(final long stationId) {
        sizeValidation();

        Section lastSection = Collections.max(sections,
            Comparator.comparingInt(o -> o.getId().intValue()));

        lastSection.isEqualTo(stationId);

        sections.remove(lastSection);
        lastSection.removeLine();
    }

    private void sizeValidation() {
        if (isOnlyOneSection()) {
            throw new IllegalStateException("구간이 한개밖에 존재하지 않습니다.");
        }
    }

    private boolean isOnlyOneSection() {
        return sections.size() == 1;
    }

    public void removeSection(final Section section) {
        sections.remove(section);
    }
}
