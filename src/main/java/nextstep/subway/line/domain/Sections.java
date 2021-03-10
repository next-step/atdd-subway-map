package nextstep.subway.line.domain;

import nextstep.subway.line.exceptions.IsDownStationExistedException;
import nextstep.subway.line.exceptions.IsNotValidUpStationException;
import nextstep.subway.line.exceptions.NotFoundSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(
        mappedBy = "line",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true
    )
    private final List<Section> sections = new ArrayList<>();


    public void add(Section section) {
        sections.add(section);
    }

    public void addStation(
        int distance,
        Line line,
        Station upStation,
        Station downStation
    ) {
        List<Station> stations = getStations();

        if (stations.size() == 0) {
            add(new Section(distance, line, upStation, downStation));
            return;
        }

        boolean isNotValidUpStation = !stations.get(stations.size() - 1)
                                               .getId()
                                               .equals(upStation.getId());
        if (isNotValidUpStation) {
            throw new IsNotValidUpStationException();
        }

        boolean isDownStationExisted = stations.stream()
                                               .anyMatch(it -> it.getId().equals(downStation.getId()));

        if (isDownStationExisted) {
            throw new IsDownStationExistedException();
        }

        add(new Section(distance, line, upStation, downStation));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                                                        .filter(it -> it.getUpStation() == finalDownStation)
                                                        .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                                                        .filter(it -> it.getDownStation() == finalDownStation)
                                                        .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeStation(Long stationId) {
        if (sections.size() <= 1) {
            throw new NotFoundSectionException();
        }
        final List<Station> stations = getStations();
        final boolean isNotValidUpStation = !stations.get(stations.size() - 1)
                                                     .getId()
                                                     .equals(stationId);

        if (isNotValidUpStation) {
            throw new IsNotValidUpStationException("하행 종점역만 삭제가 가능합니다.");
        }

        sections.stream()
            .filter(it -> it.getDownStation().getId().equals(stationId))
            .findFirst()
            .ifPresent(sections::remove);
    }
}
