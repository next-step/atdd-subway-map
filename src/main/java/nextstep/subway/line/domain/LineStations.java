package nextstep.subway.line.domain;

import nextstep.subway.line.exception.RemoveNonExistStationInLineException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class LineStations {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
    List<LineStation> lineStations = new ArrayList<>();

    public void add(LineStation lineStation) {
        // 역 중간 등록 시 재배열
        reorderWhenBetweenStations(lineStation);

        this.lineStations.add(lineStation);
    }

    private void reorderWhenBetweenStations(LineStation lineStation) {
        this.lineStations.stream()
                .filter(it -> it.equalsPreStationId(lineStation.getPreStationId()))
                .findFirst()
                .ifPresent(it -> it.updatePreStationId(lineStation.getStationId()));
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public List<LineStation> getOrderedLineStations() {
        // 출발 지점 찾기
        Optional<LineStation> preLineStation = lineStations.stream()
                .filter(it -> it.getPreStationId() == null)
                .findFirst();

        List<LineStation> result = new ArrayList<>();

        // 출발 지점의 stationId를 preStation으로 가지는지 확인 후 순차적으로 추가
        while(preLineStation.isPresent()) {
            LineStation preStation = preLineStation.get();
            result.add(preStation);

            preLineStation = lineStations.stream()
                    .filter(it -> it.getPreStationId() == preStation.getStationId())
                    .findFirst();
        }
        return result;
    }

    public void removeLineStation(Long stationId) {
        // linestation 중 stationId 가 같은 것을 가져옴
        LineStation lineStation = lineStations.stream()
                .filter(it -> it.getStationId() == stationId)
                .findFirst()
                .orElseThrow(RemoveNonExistStationInLineException::new);

        lineStations.stream()
                .filter(it -> it.getPreStationId() == stationId)
                .findFirst()
                .ifPresent(it -> it.updatePreStationId(lineStation.getPreStationId()));
        lineStations.remove(lineStation);
    }
}
