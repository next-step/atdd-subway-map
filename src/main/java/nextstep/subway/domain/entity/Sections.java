package nextstep.subway.domain.entity;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @Transient
    private final static String UP_STATION_SET = "UP_STATION_SET";
    @Transient
    private final static String DOWN_STATION_SET = "DOWN_STATION_SET";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(final Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();

        final Station endingStation = getEndingStation();
        if (!endingStation.equals(upStation)) {
            throw new IllegalArgumentException("노선에 등록된 하행 종점 역이 아닌 역을 상행 역으로 설정할 수 없습니다.");
        }

        getStations().stream().filter(downStation::equals).findAny().ifPresent(station -> {
            throw new IllegalArgumentException("이미 노선에 구간 으로 등록된 역을 하행 역으로 설정할 수 없습니다.");
        });

        sections.add(section);
    }

    public void remove(final Station station) {
        final Station endingStation = getEndingStation();
        if (!endingStation.equals(station)) {
            throw new IllegalArgumentException("하행 종점 역이 아니면 제거할 수 없습니다.");
        }

        if (sections.size() <= 1) {
            throw new IllegalArgumentException("구간이 한 개 이하면 제거할 수 없습니다.");
        }

        final Section section = sections.stream()
                .filter(_section -> _section.getDownStation().equals(station))
                .findAny().orElseThrow(IllegalStateException::new);
        sections.remove(section);
    }

    public List<Station> getStations() {
        final Map<Station, Station> stationMap = getStationMap();
        final Map<String, Set<Station>> stationSetMap = getStationSetMap(stationMap);
        final Set<Station> upStations = stationSetMap.get(UP_STATION_SET);

        final List<Station> stations = new ArrayList<>();
        Station nextStation = getStartingStation();
        while (upStations.contains(nextStation)) {
            stations.add(nextStation);
            nextStation = stationMap.get(nextStation);
        }
        stations.add(nextStation);
        return stations;
    }

    private Map<Station, Station> getStationMap() {
        return sections.stream().collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }

    private Map<String, Set<Station>> getStationSetMap(final Map<Station, Station> stationMap) {
        final Map<String, Set<Station>> stationSetMap = new HashMap<>();
        stationSetMap.put(UP_STATION_SET, stationMap.keySet());
        stationSetMap.put(DOWN_STATION_SET, new HashSet<>(stationMap.values()));
        return stationSetMap;
    }

    private Map<String, Set<Station>> getStationSetMap() {
        return getStationSetMap(getStationMap());
    }

    private Station getStartingStation() {
        final Map<String, Set<Station>> stationSetMap = getStationSetMap();
        return stationSetMap.get(UP_STATION_SET).stream()
                .filter(Predicate.not(stationSetMap.get(DOWN_STATION_SET)::contains))
                .findAny().orElseThrow(IllegalStateException::new);
    }

    private Station getEndingStation() {
        final Map<String, Set<Station>> stationSetMap = getStationSetMap();
        return stationSetMap.get(DOWN_STATION_SET).stream()
                .filter(Predicate.not(stationSetMap.get(UP_STATION_SET)::contains))
                .findAny().orElseThrow(IllegalStateException::new);
    }
}
