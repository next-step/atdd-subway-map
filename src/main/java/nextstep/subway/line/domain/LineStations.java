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

    public void add(LineStation station) {
        if (this.lineStations.isEmpty()) {
            this.lineStations.add(station);
            return;
        }

        if (station.getPreStationId() == null) {
            throw new RuntimeException();
        }

        final int loop = this.lineStations.size();
        for (int i = 0; i < loop; i++) {
            final LineStation lineStation = this.lineStations.get(i);

            if (Objects.equals(lineStation.getStationId(), station.getPreStationId())) {
                if ((i + 1) != this.lineStations.size()) {
                    final LineStation next = this.lineStations.get(i + 1);
                    next.changePreStation(station.getStationId());
                }

                this.lineStations.add(i + 1, station);
                return;
            }
        }

        throw new RuntimeException();
    }

    public List<LineStation> getLineStations() {
        return Collections.unmodifiableList(this.lineStations);
    }
}
