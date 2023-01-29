package subway.domain;

import subway.error.exception.BusinessException;
import subway.error.exception.ErrorCode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    void init(final Line line, final Station upStation, final Station downStation, final int distance) {
        final Section section = new Section(line, upStation, downStation, distance);
        this.sections.add(section);
    }

    void addSection(final Line line, final Station upStation, final Station downStation, final int distance) {
        validateSectionBeforeAdd(upStation, downStation);
        final Section section = new Section(line, upStation, downStation, distance);
        this.sections.add(section);
    }

    List<Station> getStations() {
        Station lastUpStation = getLastUpStation();
        final List<Station> stations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        stations.add(0, lastUpStation);
        return stations;
    }

    void removeSection(final Station station) {
        validateBeforeRemoveStation(station);
        final Section lastSection = this.sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst().get();
        this.sections.remove(lastSection);
    }

    private void validateSectionBeforeAdd(final Station upStation, final Station downStation) {
        if (!isLastDownStation(upStation)) {
            throw new BusinessException(ErrorCode.CANNOT_ADD_SECTION_WITH_INVALID_UP_STATION);
        }
        if (isExistsStationInLine(downStation)) {

            throw new BusinessException(ErrorCode.CANNOT_ADD_SECTION_WITH_ALREADY_EXISTS_STATION_IN_LINE);
        }
    }

    private boolean isLastDownStation(final Station station) {
        return station.equals(getLastDownStation());
    }

    private void validateBeforeRemoveStation(final Station station) {
        if (!isExistsStationInLine(station)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_NOT_EXISTS_STATION_IN_LINE);
        }
        if (!isLastDownStation(station)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_NOT_LAST_SECTION);
        }
        if (this.sections.size() < 2) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_LAST_REMAINING_SECTION);
        }
    }

    private boolean isExistsStationInLine(final Station station) {
        return  getStations().contains(station);
    }

    private Station getLastUpStation() {
        Station lastUpStation = this.sections.stream().findFirst().get().getUpStation();
        while (true) {
            final Station finalUpStation = lastUpStation;
            final Optional<Section> findSection = this.sections.stream()
                    .filter(section -> section.getDownStation().equals(finalUpStation))
                    .findAny();
            if (findSection.isEmpty()) break;
            lastUpStation = findSection.get().getUpStation();
        }
        return lastUpStation;
    }

    private Station getLastDownStation() {
        Station lastDownStation = this.sections.stream().findFirst().get().getDownStation();
        while (true) {
            final Station finalLastDownStation = lastDownStation;
            final Optional<Section> findSection = this.sections.stream()
                    .filter(section -> section.getUpStation().equals(finalLastDownStation))
                    .findAny();
            if (findSection.isEmpty()) break;
            lastDownStation = findSection.get().getDownStation();
        }
        return lastDownStation;
    }
}
