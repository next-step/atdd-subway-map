package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.DuplicatedStationException;
import nextstep.subway.exception.NoLastStationException;
import nextstep.subway.exception.SectionRegistrationException;
import nextstep.subway.exception.SectionRemovalException;

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
        if (containsOrEmpty(section)) {
            sections.add(section);
        }
    }

    public void removeSection(Station station) {
        validateLastStation(station);
        validateSingleSection();
        sections.remove(sections.size() - 1);
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(getLastSection()
                .getDownStation());
        return stations;
    }

    private boolean containsOrEmpty(Section section) {
        if (sections.isEmpty()) {
            return true;
        }
        return getStations().contains(section.getUpStation());
    }

    private void validateSingleSection() {
        if (sections.size() == 1) {
            throw new SectionRemovalException();
        }
    }

    private void validateLastStation(Station station) {
        Station lastStation = getLastSection().getDownStation();
        if (!lastStation.equals(station)) {
            throw new NoLastStationException();
        }
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }


    private void validateNewUpStation(Section section) {
        if (sections.isEmpty()) {
            return;
        }
        Station upStation = section.getUpStation();
        Station lastStation = getLastSection()
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

}
