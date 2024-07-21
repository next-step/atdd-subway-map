package subway.line.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.common.exception.SubwayException;
import subway.common.exception.SubwayExceptionType;
import subway.line.exception.InvalidDownStationException;
import subway.line.exception.InvalidUpStationException;
import subway.station.domain.Station;

@Embeddable
@Getter
@NoArgsConstructor
public class LineSections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<LineSection> lineSections = new ArrayList<>();

    public void addSection(LineSection lineSection) {

        validateUpStation(lineSection.getUpStation());
        validateDownStation(lineSection.getDownStation());

        lineSections.add(lineSection);
    }

    public void deleteSection(Station station) {
        if(lineSections.size() <= 1) {
            throw new SubwayException(SubwayExceptionType.CANNOT_DELETE_SINGLE_SECTION);
        }
        if(isDifferentFromLastDownStation(station)) {
            throw new SubwayException(SubwayExceptionType.CANNOT_DELETE_NON_LAST_DOWN_STATION);
        }
        lineSections.remove(lineSections.size() - 1);
    }

    private void validateUpStation(Station upStation) {
        if (isDifferentFromLastDownStation(upStation)) {
            throw new InvalidUpStationException(upStation.getId());
        }
    }

    private boolean isDifferentFromLastDownStation(Station station) {
        Optional<Station> lastDownStation = getLastDownStation();
        return lastDownStation.isPresent() && !lastDownStation.get().equals(station);
    }

    private Optional<Station> getLastDownStation() {
        Optional<LineSection> lastSection = getLastLineSection();
        return lastSection.map(LineSection::getDownStation);
    }

    private Optional<LineSection> getLastLineSection() {
        return lineSections.isEmpty() ? Optional.empty() : Optional.of(lineSections.get(lineSections.size() - 1));
    }

    private void validateDownStation(Station downStation) {
        if (getStations().stream()
            .anyMatch(station -> station.equals(downStation))) {
            throw new InvalidDownStationException(downStation.getId());
        }
    }

    public List<Station> getStations() {
        return lineSections.stream()
            .flatMap(lineSection -> Stream.of(
                lineSection.getUpStation(),
                lineSection.getDownStation()))
            .distinct()
            .collect(Collectors.toList());
    }
}
