package nextstep.subway.domain;

import nextstep.subway.exception.InvalidDownStationException;
import nextstep.subway.exception.InvalidUpStationException;
import nextstep.subway.exception.RemoveSectionFailException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MIN_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void removeSection(Long stationId) {
        validateSectionCount();
        validateIsLastStation(stationId);
        sections.remove(getLastSection());
    }

    public void addSection(Section section) {
        if (!sections.isEmpty()) {
            validateNewSection(section);
        }

        this.sections.add(section);
    }

    private void validateIsLastStation(Long stationId) {
        if (!isLastStation(stationId)) {
            throw new RemoveSectionFailException();
        }
    }

    private void validateSectionCount() {
        if (sections.size() == MIN_SECTIONS_SIZE) {
            throw new RemoveSectionFailException();
        }
    }

    private void validateNewSection(Section section) {
        validateUpStation(section.getUpStation());
        validateDownStation(section.getDownStation());
    }

    private void validateUpStation(Station station) {
        if (!isLastStation(station)) {
            throw new InvalidUpStationException(station.getName());
        }
    }

    private boolean isLastStation(Station station) {
        return isLastStation(station.getId());
    }

    private boolean isLastStation(Long stationId) {
        return getLastDownStation().isEqualTo(stationId);
    }

    private Station getLastDownStation() {
        return getLastSection().getDownStation();
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    private void validateDownStation(Station station) {
        sections.stream()
                .filter(s -> s.contains(station))
                .findAny()
                .ifPresent(s -> {
                    throw new InvalidDownStationException(station.getName());
                });
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(getFirstStation());
        stations.addAll(
                sections.stream()
                        .map(Section::getDownStation)
                        .collect(Collectors.toList())
        );

        return stations;
    }

    private Station getFirstStation() {
        return sections.get(0)
                .getUpStation();
    }
}
