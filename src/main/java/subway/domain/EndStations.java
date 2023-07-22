package subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import subway.exception.EndStationNotFoundException;
import subway.exception.EndStationsNotPairedException;
import subway.exception.EndStationsSameEndStationsException;

@Embeddable
public class EndStations {
    public static final int END_STATIONS_VALID_SIZE = 2;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    Set<EndStation> endStations;

    public EndStations() {}

    public EndStations(Set<EndStation> endStations) {
        this.endStations = paired(endStations);
    }

    public static EndStations of(EndStation upStation, EndStation downStation) {
        return new EndStations(new HashSet<>(Set.of(upStation, downStation)));
    }

    public EndStations clone() {
        return new EndStations(new HashSet<>(endStations));
    }

    public EndStation upEndStation() {
        return endStations.stream()
            .filter(s -> s.getDirectionType().equals(DirectionType.UP))
            .findFirst()
            .orElseThrow(() -> new EndStationNotFoundException("상행종착역이 존재하지 않습니다."));
    }

    public EndStation downEndStation() {
        return endStations.stream()
            .filter(s -> s.getDirectionType().equals(DirectionType.DOWN))
            .findFirst()
            .orElseThrow(() -> new EndStationNotFoundException("하행종착역이 존재하지 않습니다."));
    }

    public void update(EndStations endStations) {
        this.endStations.clear();
        this.endStations.addAll(endStations.endStations);
    }

    public boolean isDownStation(Station station) {
        return endStations.stream()
            .filter(s -> s.getDirectionType().equals(DirectionType.DOWN))
            .findAny()
            .orElseThrow(() -> new EndStationNotFoundException("하행종착역이 존재하지 않습니다."))
            .getStation()
            .equals(station);
    }

    private Set<EndStation> paired(Set<EndStation> endStations) {
        if (isNotValidSize(endStations) || isUniDirectional(endStations)) {
            throw new EndStationsNotPairedException("라인의 종착역은 상행종착역과 하행종착역의 짝으로 이루어져야 합니다: " + endStations);
        }
        if (endStations.stream().map(EndStation::getStation).collect(Collectors.toSet()).size() == 1) {
            throw new EndStationsSameEndStationsException("라인의 종착역은 동일한 역으로 설정할 수 없습니다: " + endStations);
        }
        return endStations;
    }

    private boolean isUniDirectional(Set<EndStation> endStations) {
        return !endStations.stream()
            .map(EndStation::getDirectionType)
            .collect(Collectors.toSet()).containsAll(List.of(DirectionType.values()));
    }

    private boolean isNotValidSize(Set<EndStation> endStations) {
        return endStations.size() != END_STATIONS_VALID_SIZE;
    }
}
