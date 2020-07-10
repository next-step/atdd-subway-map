package nextstep.subway.line.domain;

import nextstep.subway.station.application.StationDuplicateException;
import nextstep.subway.station.application.StationNotFoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class LineStations {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
    private List<LineStation> lineStations = new ArrayList<>();

    public void appendStation(LineStation lineStation) {
        if (lineStation.getStationId() == null) {
            throw new StationNotFoundException();
        }

        if (lineStations.stream().anyMatch(it -> it.isSame(lineStation))) {
            throw new StationDuplicateException();
        }

        for (int i = 0; i < lineStations.size(); i++) {
            LineStation findLineStation = lineStations.get(i);
            updatePreStationByAppend(findLineStation, lineStation);
        }
        lineStations.add(lineStation);
    }

    private void updatePreStationByAppend(LineStation findLineStation, LineStation lineStation) {
        if (findLineStation != null && findLineStation.getPreStationId() == lineStation.getPreStationId()) {
            findLineStation.updatePreStation(lineStation.getStationId());
        }
    }

    public void removeStation(Long stationId) {
        LineStation lineStation = lineStations.stream()
                .filter(it -> it.getStationId() == stationId)
                .findFirst()
                .orElseThrow(StationNotFoundException::new);

        for (int i = 0; i < lineStations.size(); i++) {
            LineStation findLineStation = lineStations.get(i);
            updatePreStationByRemove(findLineStation, lineStation);
        }
        lineStations.remove(lineStation);
    }

    private void updatePreStationByRemove(LineStation findLineStation, LineStation lineStation) {
        if (findLineStation != null && findLineStation.getPreStationId() == lineStation.getStationId()) {
            findLineStation.updatePreStation(lineStation.getPreStationId());
        }
    }

    public List<LineStation> getOrderLineStations() {
        Optional<LineStation> preLineStation = lineStations.stream()
                .filter(it -> it.getPreStationId() == null)
                .findFirst();

        List<LineStation> result = new ArrayList<>();
        while (preLineStation.isPresent()) {
            LineStation preStationId = preLineStation.get();
            result.add(preStationId);
            preLineStation = lineStations.stream()
                    .filter(it -> it.getPreStationId() == preStationId.getStationId())
                    .findFirst();
        }
        return result;
    }
}
