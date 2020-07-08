package nextstep.subway.line.domain;




import nextstep.subway.station.application.StationDuplicateException;

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
            throw new RuntimeException();
        }

        if (lineStations.stream().anyMatch(it -> it.isSame(lineStation))) {
            throw new StationDuplicateException();
        }

        for (int i = 0; i < lineStations.size(); i++) {
            LineStation findLineStation = lineStations.get(i);
            updatePreStation(lineStation, findLineStation);
        }

        lineStations.add(lineStation);
    }

    private void updatePreStation(LineStation lineStation, LineStation findLineStation) {
        if (findLineStation.getPreStationId() == lineStation.getPreStationId()) {
            findLineStation.updatePreStation(lineStation.getStationId());
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
