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

    public List<LineStation> getLineStations() {
        return Collections.unmodifiableList(this.lineStations);
    }
}
