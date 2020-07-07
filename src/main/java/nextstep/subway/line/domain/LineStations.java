package nextstep.subway.line.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class LineStations {
    @ElementCollection
    @CollectionTable(name = "line_stations")
    @OrderColumn
    private List<LineStation> lineStations;

    protected LineStations() {
        this.lineStations = new ArrayList<>();
    }

    public void add(LineStation newLineStation) {
        if (this.containsLineStation(newLineStation.getStationId())) {
            throw new IllegalStateException();
        }

        if (this.lineStations.isEmpty()) {
            this.append(newLineStation);
            return;
        }

        if (Objects.isNull(newLineStation.getPreStationId())) {
            this.prepend(newLineStation);
            return;
        }

        final int lineStationsSize = this.lineStations.size();
        for (int i = 0; i < lineStationsSize; i++) {
            final LineStation lineStation = this.lineStations.get(i);

            if (lineStation.isPreStationOf(newLineStation)) {
                this.insert(i + 1, newLineStation);
                return;
            }
        }

        throw new RuntimeException();
    }

    private boolean containsLineStation(Long stationId) {
        return this.lineStations.stream()
                .anyMatch(it -> Objects.equals(it.getStationId(), stationId));
    }

    private void append(LineStation lineStation) {
        this.lineStations.add(lineStation);
    }

    private void prepend(LineStation lineStation) {
        final LineStation firstStation = this.lineStations.get(0);
        firstStation.changePreStation(lineStation);
        this.lineStations.add(0, lineStation);
    }

    private void insert(int index, LineStation lineStation) {
        if (index != this.lineStations.size()) {
            final LineStation next = this.lineStations.get(index);
            next.changePreStation(lineStation);
        }

        this.lineStations.add(index, lineStation);
    }

    public void removeStation(Long stationId) {
        LineStation lineStation = this.lineStations.stream()
                .filter(station -> Objects.equals(station.getStationId(), stationId))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        int index = this.lineStations.indexOf(lineStation);
        this.lineStations.remove(index);

        if (index != this.lineStations.size()) {
            LineStation previousLineStation = this.lineStations.get(index - 1);
            LineStation nextLineStation = this.lineStations.get(index);
            nextLineStation.changePreStation(previousLineStation);
        }
    }

    public List<LineStation> getLineStations() {
        return Collections.unmodifiableList(this.lineStations);
    }
}
