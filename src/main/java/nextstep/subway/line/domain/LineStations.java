package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Embeddable
public class LineStations {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<LineStation> lineStations = new ArrayList<>();

    public void add(LineStation lineStation) {
        validateNotExist(lineStation);

        lineStations.stream()
                .filter(it -> it.isEqualPreStation(lineStation.getPreStationId()))
                .findFirst()
                .ifPresent(it -> it.relocateAfter(lineStation.getStationId()));

        lineStations.add(lineStation);
    }

    private void validateNotExist(LineStation lineStation) {
        boolean isExist = lineStations.stream()
                .anyMatch(l -> l.isEqualStation(lineStation.getStationId()));

        if (isExist) {
            throw new IllegalArgumentException();
        }
    }

    public void remove(Long stationId) {
        LineStation lineStation = findStationById(stationId);

        lineStations.stream()
                .filter(it -> it.isEqualPreStation(stationId))
                .findFirst()
                .ifPresent(it -> it.relocateAfter(lineStation.getPreStationId()));

        lineStations.remove(lineStation);
    }

    private LineStation findStationById(Long stationId) {
        return lineStations.stream()
                .filter(it -> it.isEqualStation(stationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<LineStation> getContent() {
        return lineStations;
    }

    public List<LineStation> getContentInOrder() {
        List<LineStation> contentInOrder = new LinkedList<>();

        for (LineStation lineStation : lineStations) {
            int order = findOrder(contentInOrder, lineStation);
            contentInOrder.add(order, lineStation);
        }

        return contentInOrder;
    }

    private int findOrder(List<LineStation> contentInOrder, LineStation lineStation) {
        return IntStream.range(0, contentInOrder.size())
                .filter(i -> contentInOrder.get(i).getStationId().equals(lineStation.getPreStationId()))
                .map(i -> i + 1)
                .findFirst()
                .orElse(contentInOrder.size());
    }
}
