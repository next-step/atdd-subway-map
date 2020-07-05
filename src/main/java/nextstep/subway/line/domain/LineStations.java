package nextstep.subway.line.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class LineStations {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
    private List<LineStation> lineStations = new ArrayList<>();

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public void add(LineStation lineStation) {
        checkValidation(lineStation);

        lineStations.stream()
                .filter(it -> it.getPreStationId() == lineStation.getPreStationId())
                .findFirst()
                .ifPresent(it -> it.updatePreStationTo(lineStation.getStationId()));

        lineStations.add(lineStation);
    }

    public List<LineStation> getStationsInOrder() {
        // 출발지점 찾기
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

    private void checkValidation(LineStation lineStation) {
        if (lineStation.getStationId() == null) {
            throw new RuntimeException();
        }

        if (lineStations.stream().anyMatch(it -> it.isSame(lineStation))) {
            throw new RuntimeException();
        }
    }
}