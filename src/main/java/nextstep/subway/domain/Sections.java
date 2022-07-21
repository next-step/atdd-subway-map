package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.DuplicatedStationException;
import nextstep.subway.exception.SectionRegistrationException;

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
        validateNewUpStation(section);
        validateNewDownStation(section);
        int index = sectionIndexOf(section);
        if (index == NO_INDEX) {
            return;
        }
        sections.add(index, section);
    }

    private void validateNewUpStation(Section section) {
        if (sections.isEmpty()) {
            return;
        }
        Station upStation = section.getUpStation();
        Station lastStation = sections.get(sections.size() - 1)
                .getDownStation();
        if (!lastStation.equals(upStation)) {
            throw new SectionRegistrationException(lastStation, upStation);
        }
    }

    private void validateNewDownStation(Section section) {
        if (sections.isEmpty()) {
            return;
        }
        Station downStation = section.getDownStation();
        if (getStations().contains(downStation)) {
            throw new DuplicatedStationException(downStation);
        }
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(sections.get(sections.size() - 1)
                .getDownStation());
        return stations;
    }

    private int sectionIndexOf(Section section) {
        if (sections.isEmpty()) {
            return ZERO;
        }
        return IntStream.range(0, sections.size())
                .filter(i -> sections.get(i)
                        .isStationEqualTo(section))
                .findFirst()
                .orElse(NO_INDEX);
    }

}
