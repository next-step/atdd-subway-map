package nextstep.subway.domain.entity;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(final Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        final Map<Station, Station> stationMap = sections.stream().collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        final Set<Station> upStations = stationMap.keySet();
        final Set<Station> downStations = new HashSet<>(stationMap.values());

        final List<Station> stations = new ArrayList<>();
        Station nextStation = upStations.stream()
                .filter(Predicate.not(downStations::contains))
                .findAny().orElseThrow(IllegalStateException::new);
        while (upStations.contains(nextStation)) {
            stations.add(nextStation);
            nextStation = stationMap.get(nextStation);
        }
        stations.add(nextStation);
        return stations;
    }
}
