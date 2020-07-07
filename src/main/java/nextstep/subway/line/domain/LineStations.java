package nextstep.subway.line.domain;

import nextstep.subway.exception.AlreadyExistsException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class LineStations {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_line_id")
    private List<LineStation> lineStations = new ArrayList<>();

    public void add(LineStation newLineStation) {
        validate(newLineStation);
        relocateExists(newLineStation);

        lineStations.add(newLineStation);
    }

    public List<LineStation> getLineStationsInOrder() {
        if (CollectionUtils.isEmpty(lineStations)) {
            return Collections.unmodifiableList(Collections.emptyList());
        }

        final LinkedList<LineStation> orderedLineStations = new LinkedList<>();

        orderedLineStations.addFirst(findStartStation());

        while (orderedLineStations.size() != lineStations.size()) {
            final Optional<LineStation> nextStation = findNextStation(orderedLineStations);

            if (!nextStation.isPresent()) {
                break;
            }
            orderedLineStations.add(nextStation.get());
        }

        return Collections.unmodifiableList(orderedLineStations);
    }

    private void validate(LineStation newLineStation) {
        if (isSameStation(newLineStation)) {
            throw new AlreadyExistsException("해당 노선에 해당 지하철역이 이미 존재합니다. : " + newLineStation.getStationId());
        }
    }

    private boolean isSameStation(LineStation newLineStation) {
        return lineStations.stream()
                .map(LineStation::getStationId)
                .anyMatch(id -> newLineStation.getStationId().equals(id));
    }

    private Optional<LineStation> findNextStation(LinkedList<LineStation> orderedLineStations) {
        return lineStations.stream()
                .filter(it -> it.isPreStation(orderedLineStations.getLast()))
                .findFirst();
    }

    private LineStation findStartStation() {
        return lineStations.stream()
                .filter(LineStation::isStartStation)
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    private void relocateExists(LineStation newLineStation) {
        lineStations.stream()
                .filter(newLineStation::isSamePreStationId)
                .findAny()
                .ifPresent(lineStation -> lineStation.updatePreStationId(newLineStation.getStationId()));
    }

    public void removeByStationId(Long stationId) {
        this.lineStations.removeIf(lineStation -> lineStation.getStationId().equals(stationId));
    }
}
