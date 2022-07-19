package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {

    private static final int NO_INDEX = -1;
    private static final int ZERO = 0;

    @OneToMany(
            mappedBy = "line",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Section> sections = new LinkedList<>();

    public void addSection(Section section) {
        int index = sectionIndexOf(section);
        if (index == NO_INDEX) {
            return;
        }
        sections.add(index, section);
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(sections.get(sections.size() - 1).getDownStation());
        return stations;
    }

    private int sectionIndexOf(Section section) {
        if (sections.isEmpty()) {
            return ZERO;
        }
        return IntStream.range(0, sections.size())
                .filter(i -> isStationEqualTo(sections.get(i), section))
                .findFirst()
                .orElse(NO_INDEX);
    }

    private boolean isStationEqualTo(Section section, Section newSection) {
        Long sectionDownStationId = section.getDownStation()
                .getId();
        Long newSectionUpStationId = newSection.getUpStation()
                .getId();
        return sectionDownStationId.equals(newSectionUpStationId);
    }

}
