package nextstep.subway.line.domain;

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
        // 역 중간에 등록
        // 같은 preStationId 역이 존재하면 기존 존재하는 preStationId에 새로 입력 된 stationId로 변경
        this.lineStations.stream()
                .filter(it -> it.equalsPreStationId(lineStation.getPreStationId()))
                .findFirst()
                .ifPresent(it -> it.updatePreStationId(lineStation.getStationId()));

        this.lineStations.add(lineStation);
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
}
