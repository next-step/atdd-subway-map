package subway.domain.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.domain.UpAndDownStationsDto;
import subway.exception.CannotDeleteStationFromALineWithOnlyTwoStationsException;
import subway.exception.DownStationOfNewSectionMustNotExistingLineStationException;
import subway.exception.ToBeDeletedStationMustBeLastException;
import subway.exception.UpStationOfNewSectionMustBeDownStationOfExistingLineException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;

@Getter
@NoArgsConstructor
@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStationsByAscendingOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<UpAndDownStationsDto> upAndDownStations = sections.stream()
                .map(Section::getUpAndDownStations)
                .collect(toUnmodifiableList());

        Station upStation = upAndDownStations.get(0).getUpStation();
        Station downStation = upAndDownStations.get(0).getDownStation();

        List<Station> stations = new ArrayList<>();
        stations.addAll(getUpperStations(upAndDownStations, upStation));
        stations.addAll(getDownerStations(upAndDownStations, downStation));

        return Collections.unmodifiableList(stations);
    }

    private List<Station> getUpperStations(List<UpAndDownStationsDto> upAndDownStations, Station upStation) {
        List<Station> upperStations = new ArrayList<>();
        upperStations.add(upStation);

        Optional<UpAndDownStationsDto> upAndDownStation =
                upAndDownStations.stream()
                .filter(dto -> dto.getDownStation().equals(upStation))
                .findFirst();
        while (upAndDownStation.isPresent()) {
            Station upperStation = upAndDownStation.get().getUpStation();
            upperStations.add(upperStation);
            upAndDownStation =
                    upAndDownStations.stream()
                    .filter(dto -> dto.getDownStation().equals(upperStation))
                    .findFirst();
        }

        Collections.reverse(upperStations);
        return upperStations;
    }

    private List<Station> getDownerStations(List<UpAndDownStationsDto> upAndDownStations, Station downStation) {
        List<Station> downerStations = new ArrayList<>();
        downerStations.add(downStation);

        Optional<UpAndDownStationsDto> upAndDownStation =
                upAndDownStations.stream()
                        .filter(dto -> dto.getUpStation().equals(downStation))
                        .findFirst();
        while (upAndDownStation.isPresent()) {
            Station downerStation = upAndDownStation.get().getDownStation();
            downerStations.add(downerStation);
            upAndDownStation =
                    upAndDownStations.stream()
                            .filter(dto -> dto.getUpStation().equals(downerStation))
                            .findFirst();
        }

        return downerStations;
    }

    public void checkSectionExtendValid(Station upStation, Station downStation) {
        List<Station> stations = getStationsByAscendingOrder();
        if (!upStation.equals(stations.get(stations.size() - 1))) {
            throw new UpStationOfNewSectionMustBeDownStationOfExistingLineException();
        }
        if (stations.stream().anyMatch(station -> station.equals(downStation))) {
            throw new DownStationOfNewSectionMustNotExistingLineStationException();
        }
    }

    public void checkSectionReduceValid(Station station) {
        List<Station> stations = getStationsByAscendingOrder();
        if (stations.isEmpty()) {
            throw new IllegalArgumentException("해당 노선이 비어있습니다.");
        }
        if (!stations.get(stations.size() - 1).equals(station)) {
            throw new ToBeDeletedStationMustBeLastException();
        }
        if (stations.size() == 2) {
            throw new CannotDeleteStationFromALineWithOnlyTwoStationsException();
        }
    }
}
