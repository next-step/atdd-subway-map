package nextstep.subway.line.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class LineStations {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_line_id")
    private List<LineStation> lineStations = new ArrayList<>();

    public List<LineStation> getLineStationsInOrder() {
        if (CollectionUtils.isEmpty(lineStations)) {
            return Collections.unmodifiableList(Collections.emptyList());
        }
        final LinkedList<LineStation> orderedLineStations = new LinkedList<>();
        final LineStation startStation = findStartStation();

        orderedLineStations.addFirst(startStation);

        while (orderedLineStations.size() != lineStations.size()) {
            final Optional<LineStation> nextStation = findNextStation(orderedLineStations);

            if (!nextStation.isPresent()) {
                break;
            }
            orderedLineStations.add(nextStation.get());
        }

        return Collections.unmodifiableList(orderedLineStations);
    }

    private Optional<LineStation> findNextStation(LinkedList<LineStation> orderedLineStations) {
        return lineStations.stream().filter(it -> it.isPreStation(orderedLineStations.getLast()))
                .findFirst();
    }

    private LineStation findStartStation() {
        return lineStations.stream()
                .filter(LineStation::isStartStation)
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    public void add(LineStation newLineStation) {
        relocateExists(newLineStation);

        lineStations.add(newLineStation);
    }

    private void relocateExists(LineStation newLineStation) {
        lineStations.stream()
                .filter(newLineStation::isSamePreStationId)
                .findAny()
                .ifPresent(lineStation -> lineStation.updatePreStationId(newLineStation.getStationId()));
    }
}
